import random
import time 

def openFile(filename):
    with open(filename, 'r') as fl:
        data = fl.read()
    return data

def openFileBiner(filename):
    with open(filename, 'rb') as fl:
        data = fl.read()
    return data

def writeFile(filename, data):
    with open(filename, 'w') as fl:
        fl.write(data)

def writeFileBiner(filename,data):
    with open(filename, 'wb') as fl:
        fl.write(data)

def xor(block, internal_key):
    block_length = len(block)
    block = int(block,2)
    internal_key = int(internal_key,2)
    result = block ^ internal_key
    result = '{0:08b}'.format(result)
    diff_length = block_length - len(result)
    if(diff_length>0):
        result = "0" * diff_length + result
    return result

def xor_block(block, internal_key):
    if(len(block)==32):
        internal_key = internal_key[0:32]
    elif(len(block)==64):
        internal_key = internal_key[0:64]
    block = xor(block,internal_key)
    return block

def generate_box_s(main_key):
    box_s = list(range(0,256))
    seed = 0
    for i in main_key:
        seed += ord(i)
    random.seed(seed)
    random.shuffle(box_s)
    
    return box_s

def subtitute_box_s(block, main_key):
    block_result = ""
    box_s = generate_box_s(main_key)
    for i in range(0, len(block), 8):
        temp = int(block[i:i+8],2)
        temp = box_s[temp]
        temp = '{0:08b}'.format(temp)
        block_result += temp
    return block_result

def bagiMatrix(teks):
    lenTeks = len(teks)
    L = ''
    R = ''
    for i in range(lenTeks):
        if(i<lenTeks/2):
            L=L+teks[i]
        else:
            R=R+teks[i]

    lengthR = len(R)
    lengthL = len(L)
    return L,R

def changeToMatrix(block,rowSize):
    blockSize = len(block)
    column = blockSize/rowSize
    Matrix = []
    componen = ''
    for i in range (blockSize):
        if(i % column == 0 and i!=0):
            Matrix.append(componen)
            componen = ''
        componen = componen+block[i]
    Matrix.append(componen)
    return Matrix

def changeFromMatrix(matrix):
    matrixSize = len(matrix)
    word = ''
    for i in range (matrixSize):
        word=word+matrix[i]
    return word

def shifLeft(data,i):
    lenght=len(data)
    temp1 = data[0:i]
    temp2 = data[i:lenght]
    data=temp2+temp1
    return data

def shifRight(data,i):
    lenght=len(data)
    temp1 = data[lenght-i:]
    temp2 = data[:lenght-i]
    data=temp1+temp2
    return data

def permutasi(block,iterasi,enc) :
    Matrix = changeToMatrix(block,4)
    column = int(len(block)/4)
    row = iterasi % 4
    nShift = iterasi % column 
    if(row != 0):
        if(enc==True):
            shifRight(Matrix[row],nShift)
        else:
            shifLeft(Matrix[row],nShift)
    Matrix = changeFromMatrix(Matrix)
    return Matrix

def fibonacci(n):
    temp0 = 1
    temp1 = 1
    if(n==0 or n==1):
        return temp1
    else :
        count = 1
        while (count<n):
            temp = temp0 + temp1
            temp0 = temp1
            temp1 = temp
            count += 1
        return temp

def renderKey(key,iterasi):
    L,R = bagiMatrix(key)
    L = changeToMatrix(L,8)
    R = changeToMatrix(R,8)
    column = int(len(L)/4)
    nShift = iterasi % column
    row = fibonacci(iterasi) % 7
    if(row != 0):
        R[row]= shifLeft(R[row],nShift)
        L[row]= shifRight(L[row],nShift)
    L = changeFromMatrix(L)
    R = changeFromMatrix(R)
    return L+R

def f_function(block, main_key, internal_key):
    block = xor_block(block, internal_key)
    block = subtitute_box_s(block, main_key)
    return block

def split_block(teks, teks_length, n_bit):
    n_blocks = teks_length//int(n_bit)
    if (teks_length % int(n_bit) > 0):
        diff_length = int(n_bit) - (teks_length % int(n_bit))
        teks = teks[:int(n_bit)*n_blocks] + "0" * diff_length + teks[int(n_bit)*n_blocks:]
        n_blocks += 1
    blocks = changeToMatrix(teks,n_blocks)
    return blocks

def encrypt(blocks,key):
    encrypted_text = ""
    for block in blocks:
        internal_key = key
        L,R = bagiMatrix(block)
        for i in range(24):
            R = permutasi(R,i,True)
            temp_R = R
            internal_key = renderKey(internal_key, i)
            hasil = f_function(R, key, internal_key)
            R = xor(hasil, L)
            L = temp_R
        hasil = L + R
        temp = ""
        for i in range(0, len(hasil), 8):
            temp += chr(int(hasil[i:i+8],2))
        encrypted_text += temp
    print("encrypted_text", encrypted_text)
    return encrypted_text.encode()

def decrypt(encrypted_text, key, n_bit):
    teks = ''.join(format(ord(x),'08b')for x in encrypted_text)
    blocks = split_block(teks, len(teks), n_bit)
    decrypted_text = ""
    list_internal_key = []
    temp_key = key
    for i in range(24):
        temp_key = renderKey(temp_key, i)
        list_internal_key.append(temp_key)

    for block in blocks:
        L,R = bagiMatrix(block)
        for i in range(23, -1, -1):
            temp_L = L
            hasil = f_function(L, key, list_internal_key[i])
            L = xor(R,hasil)
            R = temp_L
            R = permutasi(R,i,False)
        hasil = L + R
        temp = ""
        for i in range(0, len(hasil), 8):
            temp += chr(int(hasil[i:i+8],2))
        decrypted_text += temp
    print("decrypted_text", decrypted_text)

def main():
    #get user input
    namafile = input("input nama file yang akan di enkripsi : ")
    teks = openFile(namafile)
    print(teks[0])
    #teks = input("Masukkan teks: ")
    key = input("Masukkan kunci: ")
    
    n_bit = input("Masukkan panjang blok (64/128/256): ")
    
    #change input to bits
    teks = ''.join(format(ord(x),'08b')for x in teks)
    print(teks)
    key = ''.join(format(ord(x),'08b')for x in key)
    
    #split text to blocks
    blocks = split_block(teks, len(teks), n_bit)
    awal = time.time()
    encrypted_text = encrypt(blocks,key)
    writeFileBiner("hasil.txt",encrypted_text)
    akhir = time.time() 
    print ("Total Waktu Proses ", akhir- awal, " Detik."  )
    awal = time.time()
    encrypted_text=openFileBiner("hasil.txt")
    decrypt(encrypted_text.decode(),key,n_bit)
    
    akhir = time.time()  
    print ("Total Waktu Proses ", akhir- awal, " Detik."  )
    
if __name__ == "__main__":
    main()
