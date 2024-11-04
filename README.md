# Cipher Project

This project implements various encryption algorithms, including substitution ciphers (Caesar, Random, Vigenère) and the RSA public-key encryption algorithm. Designed as a command-line application, this project allows users to create, save, encrypt, and decrypt messages using different cipher techniques.

## Features

- **Caesar Cipher**: Encrypts and decrypts text by shifting each letter by a specified number of positions.
- **Random Substitution Cipher**: Creates a randomized mapping between plaintext and ciphertext characters.
- **Vigenère Cipher**: Uses a repeating key to shift characters, offering more security than simple Caesar.
- **RSA Encryption**: Implements public-key encryption for secure data transfer.

## Usage

The application can be run via the command line with customizable flags to specify cipher type, encryption/decryption options, and output choices.

### Example Commands

- **Caesar Cipher Encryption**:  
  ```bash
  java -jar <your_jar> --caesar 3 --em "Hello World" --print

## Requirements

- **Java 21**: The project uses Java 21 features and requires JDK 21.
- **JUnit**: For testing. The project includes test cases for each cipher.

## Project Structure

- **AbstractCipher**: Abstract class for common cipher functionality.
- **CipherFactory**: Factory pattern to instantiate different ciphers.
- **Main.java**: Command-line interface to manage user input and file I/O.

*Disclaimer: I do not own this project; it is from the assignment of the course CS2112 at Cornell University.*
