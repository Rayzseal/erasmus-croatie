% Program MT04_ADPCM_govor

clear
save_aud=0;         % sprema sve signale u .wav file-ove
if (save_aud),      % file-name prefix za wav file-ove
   aud_pref='CL_glo_H3_p10';
end;

P=2*menu('Order of predictors','2','4','6','8','10');

if (0),
   H=2;             % fiksna izlazna entropija
else,               % odabir zeljene entropije
   Hm=[1.5 2 2.5 3 3.5 4 5 6 7 8];
   %figure(8);
   H=Hm(menu('Entropy','1.5','2','2.5','3','3.5','4','5','6','7','8'));
end;

[y,fs]=audioread('samo[1].wav'); % Ucitaj signal

if (0),
   %plot(y);         % Prikazi ga
   t=ginput(2);     % oznaci dio signala za obradu
   t=round(t(:,1));
else,               % Odabir jednog oznacenog glasa
   %figure(9);
   gl=menu('vowel','a','e','i','o','u','3'); 
   in=[             % pocetni i zavrsni indeks glasa
       752        4484 
        7440       11276
       13868       18378
       20919       25274
       27814       32272
       34865       39064];
   t=in(gl,:);
end;

x=y(t(1):t(2));     % Izdvoji odabrani glas
x=x/max(abs(x));    % normaliziraj na +/-1 
N=length(x);        % Broj uzoraka
%figure(1);
%plot(x);            % Prikazi ga
%title('The waveform of the selected voice');
%soundsc(x,fs)       % reproduciraj original
%pause;

Hi=[0.5:0.5:8]; 
SNRx_graf = zeros(size(Hi)); 
SNRxr_graf = zeros(size(Hi)); 
HIx_graf = zeros(size(Hi)); 
HIe_graf = zeros(size(Hi));






% Odredi linerani prediktor (LPC) za odabrani glas
R=xcorr(x,P);       % autokorelacija signala
R=R(P+[1:P+1]);     % samo pozitivni dio 0..P
M=toeplitz(R(1:P)); % formiraj matricu sustava
FI=R(2:P+1);        % slobodni stupac
al=inv(M)*FI;       % koeficijenti prediktora
A=[1 -al'];         % inverzni filtar
%[HH,om]=freqz(1,A,1024);   % frekv. karak. LPC modela
%plot(om/pi*fs/2,20*log10(abs(HH)));
title('Prediktivni LPC model spektra signala')
xlabel('Frequencies [Hz]');
ylabel('Amplitude [dB]');
pause;

eo=filter(A,1,x);   % signal pogreske predikcije za
                    % slucaj otvorene petlje (savrsene
                    % kvantizacije H=Inf)

% Odredi diferencijalne entropije originalnog signala
% i predkicijske pogreske sa otvorenom petljom predikcije
[hX,sig2_x,SQNR0x]=MT04_diff_entr(x,1);
[hEO,sig2_eo,SQNR0eo]=MT04_diff_entr(eo,0);

PGo=10*log10(sig2_x/sig2_eo);  % dobitak predikcije otvorene petlje
for Hidx = 1:length(Hi) 
    H = Hi(Hidx); 
    %
    Dx=2^(hX-H);        % Korak kvantizacije za x
    De=2^(hEO-H);       % i za predkicijsku pogresku e otvorene petlje
    
    for jj=1:4,         % Nekoliko prolaza postupka direktne kvantizacije
                    % (bez predikcije)
    xi=round(x/Dx);     % izlazni indeksi kvantizatora
    xq=Dx*xi;           % kvantizirani signal
    erx=x-xq;           % prediction quantization error
    Ex_er=mean(erx.^2); % ocekivanje kvadrata pogreske
    SNRx=10*log10(sig2_x/Ex_er);  % Stvarni odnos signal sum [dB]
    kodx=[min(xi):max(xi)];       % svi kodovi od min do max
    pdfx=hist(xi,kodx)/length(x); % pdf indeksa
    kod_postoji=find(pdfx>0);     % kodovi koji se koriste
    HIx=-pdfx(kod_postoji)*log2(pdfx(kod_postoji))'; % Entropija
    fprintf('\n Passage jj=%d - Entropy Signal xq, H(I_X)= %.3f',jj,HIx);
    deH=HIx-H;          % Razlika izmedju stvarne i zeljene izlazne entropije
    % Kako bi ostvarili bas zeljenu entropiju, potrebno je modificirati
    % kvantizacijski korak sa faktorom: 2^deH
    Dx=Dx*2^deH;        % napravi jos jedan prolaz direktne kvatizacije
    end;
    
    fprintf('\n\n');
    for jj=1:4,         % Nekoliko prolaza ADPCM postupka kvantizacije
    st=zeros(1,P);      % vektor stanja prediktora
    xp=0*x;             % vektor za signal predikcije
    xrq=0*x;            % rekonstruirani kvantizirani signal
    ec=0*x;             % predikcijska pogreska u zatovrenoj petlji
    eci=0*x;            % izlazni kvantizacijski indeks
    ecq=0*x;            % kvantizirana pogreska predikcije
    
    %need to find error here mS04
    %error was in vector st
    % xq = quantized value of signal
    %input xrq : ecq + xp 
    for i=1:N,                  % for all samples
        xp(i)=st*al;            % calculate the prediction
        ec(i)=x(i)-xp(i);       % prediction error
        eci(i)=round(ec(i)/De); % quantization index
        ecq(i)=De*eci(i);       % quantized prediction error
        xrq(i)=ecq(i)+xp(i);    % reconstructed quantized signal
        st=[xrq(i) st(1:P-1)];   % update predictor states = state prediction vector
    end;
    kode=[min(eci):max(eci)];   % svi kodovi od min do max
    pdfe=hist(eci,kode)/N;      % pdf izlaznih indeksa
    kod_postoji=find(pdfe>0);   % kodovi koji se koriste
    HIe=-pdfe(kod_postoji)*log2(pdfe(kod_postoji))';
    fprintf('\n Prolaz jj=%d - Entropija signala eq, H(I_E)= %.3f',jj,HIe);
    deH=HIe-H;          % Razlika izmedju stvarne i zeljene izlazne entropije
    % Kako bi ostvarili zeljenu entropiju, potrebno je modificirati
    % kvantizacijski korak sa faktorom: 2^deH
    De=De*2^deH;        % napravi jos jedan prolaz ADPCM kvatizacije
    end;   


    ere=ec-ecq;                 % Kvantizacijska pogreska za signal
                                % predikcijske pogreske
    Eec=mean(ec.^2);            % ocekivanje kvadrata pogreske predikcije
    Eec_er=mean(ere.^2);        % ocekivanje kvadrata kvantizacijske pogreske
                                % za signal predikcijske pogreske
    SNRe=10*log10(Eec/Eec_er);  % Stvarni odnos signal sum [dB] za signal e
    
    erxr=x-xrq;                 % razlika rekonstrukcije u odnosu na polazni signal
    Exr_er=mean(erxr.^2);       % ocekivanje kvadrata pogreske
    SNRxr=10*log10(sig2_x/Exr_er); % Stvarni odnos signal sum [dB] za signal x
                                   % kvantiziran u petlji prediktora
    disp(max(abs(ere-erxr)))    % kvant. pogreska za e i za x moraju biti iste!
    PGc=10*log10(sig2_x/Eec);   % dobitak predikcije zatvorene petlje
    
    SNRx_graf(Hidx) = SNRx; 
    SNRxr_graf(Hidx) = SNRxr; 
    HIx_graf(Hidx) = HIx; 
    HIe_graf(Hidx) = HIe; 
end 
figure(1);
title('direct and predictive coding case'); xlabel('Entropy Hi'); ylabel('ratio of the signal and quantization noise sB');
plot(HIx_graf, SNRx_graf);
hold on
plot(HIe_graf, SNRxr_graf, 'red')
hold off

pause;

                               
fprintf('\n Stvarne izlazne entropije: H(I_X)=%.3f H(I_E)=%.3f',HIx, HIe);
fprintf('\n Stvarni izlazni SNR: SNR(X)=%.3f dB SNR(E)=%.3f dB',SNRx, SNRe);
fprintf('\n Stvarni izlazni SNR nakon rekonstrukcije: SNR(Xr)=%.3f dB',SNRxr);
fprintf('\n Predikcijski dobitak: PGotv =%.3f dB i PGzatv =%.3f dB',PGo,PGc);
fprintf('\n Povecanje kvalitete: SNR(Xr)-SNR(X)=%.3f dB\n\n',SNRxr-SNRx);

% Izracunaj spektre svih signala u strukturi (radi prikaza)
sp_x=20*log10(abs(freqz(x,1,floor(N/2))));
sp_xp=20*log10(abs(freqz(xp,1,floor(N/2))));
sp_xq=20*log10(abs(freqz(xq,1,floor(N/2))));
sp_ec=20*log10(abs(freqz(ec,1,floor(N/2))));
sp_xrq=20*log10(abs(freqz(xrq,1,floor(N/2))));
sp_x_xq=20*log10(abs(freqz(x-xq,1,floor(N/2))));
sp_x_xrq=20*log10(abs(freqz(x-xrq,1,floor(N/2))));
sp_ec_ecq=20*log10(abs(freqz(ec-ecq,1,floor(N/2))));
om=[0:floor(N/2)-1]/N*fs;   % frekvencijska os
Np=150;                     % broj uzoraka za vremenski prikaz
in=round(N/2+[-Np/2:Np/2]); % centralni indeksi signala za prikaz
mil=-20; mal=50;            % y-raspon za prikaz spektra [dB]

% Prikazi sve signale u vremenskoj i spektralnoj domeni
figure(1);
subplot(2,1,1); plot(x(in)); axis([1 Np -1 1]);
title('x - Originalni signal'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_x); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(2);
subplot(2,1,1); plot(xp(in)); axis([1 Np -1 1]);
title('xp - Predikcija signala'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_xp); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(3);
subplot(2,1,1); plot(ec(in)); axis([1 Np -1 1]);
title('ec - Pogreska predikcije'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_ec); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(4);
subplot(2,1,1); plot(xq(in)); axis([1 Np -1 1]);
title('xq - Direktno kvantizirani signal'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_xq); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(5);
subplot(2,1,1); plot(xrq(in)); axis([1 Np -1 1]);
title('xrq - Prediktivno kvantizirani signal'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_xrq); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(6);
subplot(2,1,1); plot(x(in)-xq(in)); axis([1 Np -1 1]);
title('(x-xq) - Pogreska direktne kvantizacije'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_x_xq); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(7);
subplot(2,1,1); plot(x(in)-xrq(in)); axis([1 Np -1 1]);
title('(x-xrq) - Pogreska prediktivne kvantizacije'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_x_xrq); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');
figure(8);
subplot(2,1,1); plot(ec(in)-ecq(in)); axis([1 Np -1 1]);
title('(ec - ecq) - Pogreska kvatizacije predikcijske pogreske'); xlabel('uzorci n'); ylabel('amplituda');
subplot(2,1,2); plot(om,sp_ec_ecq); axis ([0 fs/2 mil mal]);
title('spektar');xlabel('Hz'); ylabel('dB');

maam=1.01*max([max(abs(xq)) max(abs(xrq)) max(abs(xp))]);
ma=max(max(abs(x-xq)),max(abs(x-xrq)))*2; % skala za prikaz greske
if (save_aud),
   audiowrite(x/maam,fs,16,[aud_pref '_x']);
   audiowrite(xp/maam,fs,16,[aud_pref '_xp']);
   audiowrite(ec/maam,fs,16,[aud_pref '_ec']);
   audiowrite(xq/maam,fs,16,[aud_pref '_xq']);
   audiowrite(xrq/maam,fs,16,[aud_pref '_xrq']);
   audiowrite((x-xq)/ma,fs,16,[aud_pref '_x_xq']);
   audiowrite((x-xrq)/ma,fs,16,[aud_pref '_x_xrq']);
   audiowrite((ec-ecq)/ma,fs,16,[aud_pref '_ec_ecq']);
end;

% Interaktivni odabir jedne od slika
odabir=1;
[gui, cmap] = imread('ADPCM.bmp', 'bmp');
figure(9), image(gui), colormap(cmap);
while(~strcmp(odabir, 'kraj')),
figure(9), odabir=MT04_buton(ginput(1));

if strcmp(odabir,'x'),
   figure(1);soundsc(x);
elseif strcmp(odabir,'xp')
   figure(2);soundsc(xp);
elseif strcmp(odabir,'ec'),
   figure(3);soundsc(ec);
elseif strcmp(odabir,'xq'),
   figure(4);soundsc(xq);
elseif strcmp(odabir,'xrq'),
   figure(5);soundsc(xrq);
elseif strcmp(odabir,'xxq'),
   figure(6);sound((x-xq)/ma);
elseif strcmp(odabir,'xxrq'),
   figure(7);sound((x-xrq)/ma);
elseif strcmp(odabir,'ececq'),
   figure(8);sound((ec-ecq)/ma);
end;
end;
close all;