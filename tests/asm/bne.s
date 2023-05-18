.text
.global	_start

_start:
	# i = 0
	# while (i < 6) i++;

	li x5, 0
	li x6, 6

loop:
	addi x5, x5, 1
	bne x5, x6, loop
<<<<<<< HEAD
=======

    unimp # x5 should be 6
>>>>>>> 5069dce (update Makefile)
