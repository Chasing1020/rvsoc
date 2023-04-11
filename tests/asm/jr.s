	.text
	.global	_start

_start:
	la x5, _start		# x5 = _start
	jr x5  # should timed out
