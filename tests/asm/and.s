.text
.global	_start

_start:
	li x6, 0x10		# x6 = 0b0001_0000
	li x7, 0x11		# x7 = 0b0001_0001
	and x5, x6, x7	# x5 = x6 & x7
