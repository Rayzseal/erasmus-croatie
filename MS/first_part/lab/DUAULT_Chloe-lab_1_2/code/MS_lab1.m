[y, fs] = audioread('samo[1].wav');
t = [ 752           0
        4484           0
        7440           0
       11276           0
       13868           0
       18378           0
       20919           0
       25274           0
       27814           0
       32272           0
       34865           0
       39064           0];

in = [t(1:2:12)' t(2:2:12)'] ;


x = y(in(2,1):in(2,2)); % selection fo the phoneme
N = length(x); % length of vector x
x = x / max(abs(x)); % normalization
H = 1; % desired output entropy

 


chan = 6;

%x = linspace(0,6,50);

HQuan= zeros(chan,1);
SNRQ= zeros(chan,1);
HENC= zeros(chan,1);
SNRENC = zeros(chan,1);

Ndct=8; % size of the DCT block 


for channel=1:6
    H=channel;
    
    nb=floor(N/Ndct); % number of whole blocks in the signal 
    x=x(1:nb*Ndct); % remove signal samples that are after the last block 
    xm=zeros(Ndct,nb);% initialize matrix for signal blocks 
    xm(:)=x; % fill the matrix columns with signal blocks
    
    dY = zeros(Ndct,1);
    
    tr=dctm(Ndct); 
    ym=tr*xm;
    
    hY=zeros(Ndct,1); 
    sig2_y=zeros(Ndct,1); 
    SQNR0y=zeros(Ndct,1);
    [hX,sig2_x,SQNR0x]=MT04_diff_entr(x,0); 
    Dx=2^(hX-H); % Quantization step size for x

    for i=1:Ndct 
     [hY(i),sig2_y(i),SQNR0y(i)]=MT04_diff_entr(ym(i,:),0);
     dY(i) = 2^(hY(i)-H); %calculate step size for each channel
    end  
    mhY=mean(hY); 
    Dy=2^(mhY-H); % Quantization step size for y 
    
    xi=round(x/Dx); % output quantizer indices 
    xq=Dx*xi; % quantized signal 
    erx=x-xq; % quantization error 
    Ex_er=mean(erx.^2); % expectation of the squared error 
    SNRx=10*log10(sig2_x/Ex_er); % Actual SNR ratio in dB  
    tablex=[min(xi):max(xi)]; % all output symbols from min to max 
    pdfx=hist(xi,tablex)/length(x);% pdf of output indices  
    kod_postoji=find(pdfx>0); % used symbols at the output  
    % Entropy of output symbols (required channel rate):  
    HIx=-pdfx(kod_postoji)*log2(pdfx(kod_postoji))';
     
    %direct quantization
    yi=round(ym/Dy); % output quantizer indices 
    yq=Dy*yi; % quantized signal 
    ery=ym-yq; % quantization error 
    Ey_eri=mean((ery').^2); % expectation of the squared error per channel 
    Ey_er=mean(Ey_eri); % total mean squared quantization error 
    Eyi=mean((ym').^2); % expectation of the squared coefficients. 
    Ey=mean(Eyi); % average energy of all coefficients

    SNRyi=10*log10(Eyi'./Ey_eri');% Actual SNR ratio in dB per channel 
    SNRy=10*log10(Ey/Ey_er); 
    % Actual SNR ratio in dB for the whole signal
    
    
    HIy=zeros(Ndct,1); 
    for i=1:Ndct 
        tabley=[min(yi(i,:)):max(yi(i,:))]; % all output symbols from min to max 
        pdfy=hist(yi(i,:),tabley)/nb; % pdf of output quantizer indices 
        out_symb = find(pdfy>0); % observed output symbols 
        HIy(i) = -pdfy(out_symb) * log2(pdfy(out_symb))'; % actual entropy of the output symbols
    end 
    HQuan(channel) = mean(HIy);
    SNRQ(channel) = SNRy;


    %step 4 and 5
    %encoder
    table=[min(yi(3,:)):max(yi(3,:))]; % all output symbols from min to max 
    pdf=hist(yi(3,:),table)/nb; % pdf of output symbols 
    
    [e, al, m] = MT02_huffman(table, pdf);% coding table and entropy 
    str = MT02_huff_enc(yi(3, :), m, 1);% huffman encoding 
    ydekod = MT02_huff_dec(str, m); % huffman decoding 
    razl = sum(abs(ydekod - yi(3, :))); % difference (should be 0) 
    
    HENC(channel) = e;
    SNRENC(channel) = al;
    m(:,1) = MT02_huffman(table, pdf);
    m = mean(m);
    huffVal(channel) = mean(m');
    %step 6 7
    
    xr=tr'*yq; 
    xr=xr(:);
    
    
    xrk = zeros(Ndct, length(xr)); 
    for i=1:Ndct 
        izdvoji=zeros(Ndct); 
        izdvoji(i,i)=1; 
        yqk=izdvoji*yq; 
        xrk_matr = tr'*yqk; 
        xrk(i,:) = xrk_matr(:); 
    end 
end
plot(HQuan, SNRQ)
hold on

plot(HENC, SNRQ)
hold on

plot(HQuan, huffVal)
hold on

plot(HENC, huffVal)
hold off

