	.text
	.global	_start

_start:
	li x6, 0xffffffab	# int x6 = 0xffffffab
	la x5, _array		# array[0] = (char)x6
	sb x6, 0(x5)
    unimp


_array:
	.byte 0x00
	.byte 0x00

	.end			# End of file

