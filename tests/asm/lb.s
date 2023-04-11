	.text			# Define beginning of text section
	.global	_start		# Define entry _start

_start:
	la x5, _array		# char *x5 = &(array[0])
	lb x6, 0(x5)		# char x6 = *x5
	lb x7, 1(x5)		# char x7 = *(x5 + 1)

    unimp

_array:
	.byte 0x11
	.byte 0x22
    .byte 0x33
    .byte 0x44

	.end			# End of file
