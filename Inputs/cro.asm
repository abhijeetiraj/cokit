ORG 0
AGAIN: MVI A,00
BACK: INR A
OUT 40
CPI 20
JNZ BACK
BACK1: DCR A
OUT 40
CPI 00
JNZ BACK1
JMP AGAIN
HLT