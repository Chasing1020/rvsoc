	.text			# Define beginning of text section
	.global	_start		# Define entry _start

_start:
	la x5, _array		# char *x5 = &(array[0])
	lbu x6, 0(x5)		# char x6 = *x5 = 0x00000011
	lbu x7, 1(x5)		# char x7 = *(x5 + 1) = 0x000000ff
    unimp

_array:
	.byte 0x11
	.byte 0xff

	.end			# End of file
