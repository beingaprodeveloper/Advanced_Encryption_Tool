# Advanced Encryption Tool (AES-256)

## 🔒 Project Description:
This is a **File Encryption and Decryption Tool** built using **Java AES-256 encryption**.  
It allows users to securely encrypt and decrypt files with a **password-based key** using `PBKDF2WithHmacSHA256`.

## 🚀 Features:
- **AES-256 Encryption (CBC Mode)**
- **Password-based key generation (PBKDF2)**
- **Salt and IV for enhanced security**
- **Simple Console-based User Interface**
- **No external libraries needed (Pure Java)**

## 🛠️ Technologies Used:
- **Java SE (JDK 8+)**
- `javax.crypto` for encryption
- No bcrypt / No Bouncy Castle

## 📂 How to Run:

1. **Compile & Run in Eclipse**  
2. Follow console instructions:
   - **1** → Encrypt a file  
   - **2** → Decrypt a file

## 📥 Inputs Required:
- Input file path  
- Output file path  
- Password

## ✅ Outputs:
- Encrypted file (`*.enc`)  
- Decrypted file (matches original)

## ⚠️ Important Notes:
- Use the **same password** for encryption and decryption.
- Do not open or modify `.enc` files manually.

## 👨‍💻 Author:
Viraj Bhoir  
