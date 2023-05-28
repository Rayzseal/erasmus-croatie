% Program MT04_ADPCM_slika

clear;
noncau=0;     % Ako ==1 koristi nekauzalni prediktor
or=menu('Red prediktora','1','2','3');
debu=1;       % Omogucuje prikaz slika u fazi odredjivanja
              % prediktora
warning off Images:truesize:imageTooBigForScreen

if (0),
   H=2;             % fiksna izlazna entropija
else,               % odabir zeljene entropije
   Hm=[1.5 2 2.5 3 3.5 4];
   H=Hm(menu('Entropija','1.5','2','2.5','3','3.5','4'));
end;

% Odaberi ulaznu sliku za obradu
dem=menu('Odaberi sliku','demo1','demo2','demo3','demo4','demo5','demo6');
x=double(rgb2gray(imread(sprintf('pandaColored.png',dem))));  % Ucitaj sliku
[M,N]=size(x);      % velicina slike
NN=M*N;             % ukupni broj pixela
mx=sum(sum(x))/NN;  % srednja vrijednost slike
x=x-mx;             % ukloni srednju vrijednost radi jednostavnije
                    % interpretacije varijance signala

% Pronadji optimali prediktor al za proces x ...
% ... slika predikcijske pogreske otvorene petlje je u varijabli 'eo'
[inw,al,alm,PG,eo]=MT04_impred(x,noncau,or,debu);

% Odredi diferencijalnu entropiju procesa i predikcijske
% pogreske za prediktor u otvorenoj petlji (procese x & eo)
[hX,sig2_x,SQNR0x]=MT04_diff_entr(x(:)+rand(size(x(:)))-0.5,1);
[hEO,sig2_eo,SQNR0eo]=MT04_diff_entr(eo(:),1);

PGo=10*log10(sig2_x/sig2_eo);  % dobitak predikcije otvorene petlje

Dx=2^(hX-H);        % Korak kvantizacije za x
De=2^(hEO-H);       % i za predkicijsku pogresku e otvorene petlje

for jj=1:10,        % Nekoliko prolaza postupka direktne kvantizacije
                    % (bez predikcije)
xi=round(x/Dx);     % izlazni indeksi ECSQ uniformnog kvantizatora
xq=Dx*xi;           % kvantizirani signal
erx=x-xq;           % pogreska kvantizatora
Ex_er=mean(erx(:).^2);        % ocekivanje kvadrata pogreske
SNRx=10*log10(sig2_x/Ex_er);  % Stvarni odnos signal sum [dB]
kodx=[min(xi(:)):max(xi(:))]; % svi kodovi od min do max
pdfx=hist(xi(:),kodx)/NN;     % pdf indeksa
kod_postoji=find(pdfx>0);     % kodovi koji se uistinu koriste
HIx=-pdfx(kod_postoji)*log2(pdfx(kod_postoji))'; % Entropija
fprintf('\n Prolaz jj=%d - Entropija signala xq, H(I_X)= %.3f',jj,HIx);
deH=HIx-H;          % Razlika izmedju stvarne i zeljene izlazne entropije
% Kako bi ostvarili bas zeljenu entropiju, potrebno je modificirati
% kvantizacijski korak sa faktorom: 2^deH
Dx=Dx*2^deH;        % napravi jos jedan prolaz direktne kvatizacije
end;                % ... sa novim korakom

fprintf('\n\n');
for jj=1:4,         % Nekoliko prolaza ADPCM postupka kvantizacije
xp=0*x;             % vektor za signal predikcije
xrq=x;              % rekonstruirani kvantizirani signal
ec=0*x;             % predikcijska pogreska u zatovrenoj petlji
eci=0*x;            % izlazni kvantizacijski indeks
ecq=0*x;            % kvantizirana pogreska predikcije
for m=1+or:M-or,    % po svim retcima (osim rubova)
   for n=1+or:N-or,    % po svim stupcima (osim rubova)
      i=(n-1)*M+m;            % linearni indeks slike
      xp(i)=xrq(i+inw)'*al;   % izracunaj predikciju
      ec(i)=x(i)-xp(i);       % pogreska predikcije
      eci(i)=round(ec(i)/De); % kvantizacijski indeks
      ecq(i)=De*eci(i);       % kvantizirana pogreska predikcije
      xrq(i)=ecq(i)+xp(i);    % rekonstruirani kvantizirani signal
   end;
end;
kode=[min(eci(:)):max(eci(:))];% svi kodovi od min do max
pdfe=hist(eci(:),kode)/NN;     % pdf izlaznih indeksa
kod_postoji=find(pdfe>0);      % kodovi koji se koriste
HIe=-pdfe(kod_postoji)*log2(pdfe(kod_postoji))'; % Entropija
fprintf('\n Prolaz jj=%d - Entropija signala eq, H(I_E)= %.3f',jj,HIe);
deH=HIe-H;          % Razlika izmedju stvarne i zeljene izlazne entropije
% Kako bi ostvarili zeljenu entropiju, potrebno je modificirati
% kvantizacijski korak sa faktorom: 2^deH
De=De*2^deH;        % napravi jos jedan prolaz ADPCM kvatizacije
end;                % ... sa novim korakom

ere=ec-ecq;                    % Kvantizacijska pogreska za signal
                               % predikcijske pogreske
Eec=mean(ec(:).^2);            % ocekivanje kvadrata pogreske predikcije
Eec_er=mean(ere(:).^2);        % ocekivanje kvadrata kvantizacijske pogreske
                               % za signal predikcijske pogreske
SNRe=10*log10(Eec/Eec_er);     % Stvarni odnos signal sum [dB] za signal ec

erxr=x-xrq;                    % razlika rekonstrukcije u odnosu na polazni signal
Exr_er=mean(erxr(:).^2);       % ocekivanje kvadrata pogreske
SNRxr=10*log10(sig2_x/Exr_er); % Stvarni odnos signal sum [dB] za signal x
                               % kvantiziran u zatvorenoj petlji prediktora
disp(max(abs(ere(:)-erxr(:)))) % kvant. pogreska za e i za x moraju biti identicne!
PGc=10*log10(sig2_x/Eec);      % dobitak predikcije zatvorene petlje
                               
fprintf('\n Stvarne izlazne entropije: H(I_X)=%.3f H(I_E)=%.3f',HIx, HIe);
fprintf('\n Stvarni izlazni SNR: SNR(X)=%.3f dB SNR(E)=%.3f dB',SNRx, SNRe);
fprintf('\n Stvarni izlazni SNR nakon rekonstrukcije: SNR(Xr)=%.3f dB',SNRxr);
fprintf('\n Predikcijski dobitak: PGotv =%.3f dB i PGzatv =%.3f dB',PGo,PGc);
fprintf('\n Povecanje kvalitete: SNR(Xr)-SNR(X)=%.3f dB\n\n',SNRxr-SNRx);

% Prikazi slike u svim karakteristicnim tockama ADPCM strukture
figure(1);
imagesc(x+mx);caxis([0 255]);
colormap('gray');colorbar;truesize(1,[M N]);
title('x - Originalna slika');

figure(2);
imagesc(xp+mx);caxis([0 255]);
colormap('gray');colorbar;truesize(2,[M N]);
title('xp - Predikcija slike');

figure(3);
imagesc(ec);colormap('gray');
colorbar;truesize(3,[M N]);
title('ec - Pogreska predikcije');

figure(4);
imagesc(xq+mx);caxis([0 255]);
colormap('gray');colorbar;truesize(4,[M N]);
title('xq - Direktno kvantizirani signal');

figure(5);
imagesc(xrq+mx);caxis([0 255]);
colormap('gray');colorbar;truesize(5,[M N]);
title('xrq - Prediktivno kvantizirani signal');

figure(6);
imagesc(x-xq);colormap('gray');
colorbar;truesize(6,[M N]);
title('(x-xq) - Pogreska direktne kvantizacije');

figure(7);
imagesc(x-xrq);colormap('gray');
colorbar;truesize(7,[M N]);
title('(x-xrq) - Pogreska prediktivne kvantizacije');

figure(8);
imagesc(ec-ecq);colormap('gray');
colorbar;truesize(8,[M N]);
title('(ec - ecq) - Pogreska kvantizacije predikcijske pogreske');

% Interaktivni odabir jedne od slika
odabir=1;
[gui, cmap] = imread('ADPCM.bmp', 'bmp');
figure(9), image(gui), colormap(cmap);
while(~strcmp(odabir, 'kraj')),
figure(9), odabir=MT04_buton(ginput(1));

if strcmp(odabir,'x'),
   figure(1);
elseif strcmp(odabir,'xp'),
   figure(2);
elseif strcmp(odabir,'ec'),
   figure(3);
elseif strcmp(odabir,'xq'),
   figure(4);
elseif strcmp(odabir,'xrq'),
   figure(5);
elseif strcmp(odabir,'xxq'),
   figure(6);
elseif strcmp(odabir,'xxrq'),
   figure(7);
elseif strcmp(odabir,'ececq'),
   figure(8);
end;
end;
close all;

