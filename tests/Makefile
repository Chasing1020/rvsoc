CROSS_COMPILE := riscv64-unknown-elf-
CFLAGS        := -nostdlib -fno-builtin -march=rv64g -mabi=lp64 -O2 -Wall -Ttext=0x80000000

GDB     := $(CROSS_COMPILE)gdb
CC      := $(CROSS_COMPILE)gcc
OBJCOPY := $(CROSS_COMPILE)objcopy
OBJDUMP := $(CROSS_COMPILE)objdump

ELF_FILES  := $(shell find . -name '*.out')
BIN_FILES  := $(shell find . -name '*.bin')
HEX_FILES  := $(shell find . -name '*.hex')
DUMP_FILES := $(shell find . -name '*.dmp')


%.out: %.s
	$(CC) $(CFLAGS) $^ -o $@
	$(OBJDUMP) -D $@ > ./dump/$@.dmp

%.bin: %.out
	$(OBJCOPY) -O binary $^ $@

%.hex: %.bin
	xxd -p -c 4 $^ > ./hex/$@

clean:
	rm $(BIN_FILES) $(ELF_FILES) $(HEX_FILES) $(DUMP_FILES)

.PHONY: clean