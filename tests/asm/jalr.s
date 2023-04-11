	.text
	.global	_start

_start:
	li x6, 1
	li x7, 2
	jal x5, sum		# call sum, return address is saved in x5

    unimp

sum:
	add x6, x6, x7		# x6 = x6 + x7
	addi x6, x6, 10
	jalr x0, x5, 0		# return 13

	.end