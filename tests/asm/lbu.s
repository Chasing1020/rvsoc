	.text			# Define beginning of text section
	.global	_start		# Define entry _start

_start:
	la x5, _array		# char *x5 = &(array[0])
	lbu x6, 0(x5)		# char x6 = *x5 = 0x00000011
	lbu x7, 1(x5)		# char x7 = *(x5 + 1) = 0x000000ff
	lbu x8, 2(x5)		# char x8 = *(x5 + 1) = 0x000000ff
	lbu x9, 3(x5)		# char x9 = *(x5 + 1) = 0x000000ff
	# lbu x10, 4(x5)		# char x10 = *(x5 + 1) = 0x000000ff
    unimp

_array:
	.2byte	0x6548
    .2byte	0x6c6c

	.end			# End of file
