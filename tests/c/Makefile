CROSS_COMPILE := riscv64-unknown-elf-
CFLAGS        := -march=rv64g -ffreestanding -nostdlib -static -O2 -Wl,-Ttext=0

GDB     := $(CROSS_COMPILE)gdb
CC      := $(CROSS_COMPILE)gcc
OBJCOPY := $(CROSS_COMPILE)objcopy
OBJDUMP := $(CROSS_COMPILE)objdump

ASM_FILES  := $(shell find . -name '*.c')
ELF_FILES  := $(shell find . -name '*.out')
BIN_FILES  := $(shell find . -name '*.bin')
HEX_FILES  := $(shell find . -name '*.hex')
DUMP_FILES := $(shell find . -name '*.dmp')

default: all
all: $(ASM_FILES)
	@for file in $^ ; do \
  		make `echo $${file} | sed "s/\.c/.hex/"`; \
	done

%.out: %.c
	$(CC) $(CFLAGS) $^ -o $@
	$(OBJDUMP) -D $@ > ./$@.dmp

%.bin: %.out
	$(OBJCOPY) -O binary $^ $@

%.hex: %.bin
	xxd -p -c 4 -g 2 $^ > ./$@
	python3 tailing.py

clean:
	rm $(BIN_FILES) $(ELF_FILES) $(HEX_FILES) $(DUMP_FILES)

.PHONY: default all clean