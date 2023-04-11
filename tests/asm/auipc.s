.text
.global	_start

_start:
    auipc x5, 0x123    # x5 = PC + 0x123 << 12
	auipc x6, 0		   # x6 = PC
