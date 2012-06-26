DATA SEGMENT
	OP1 DW 1111H
	OP2 DW 1111H
	OP3 DW 2222H
	OP4 DW 2222H
	RE1 DW ?
	RE2 DW ?
	RE3 DW ?
	RE4 DW ?
DATA ENDS

CODE SEGMENT
MAIN PROC FAR
ASSUME CS:CODE,DS:DATA
START:	MOV AX,DATA
	MOV DS,AX
        
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX
        ADD AX,AX

	       

	INT 3H
MAIN ENDP
CODE ENDS
	END START