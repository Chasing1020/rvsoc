BASE_DIR  := $(abspath .)
BUILD_DIR := $(BASE_DIR)/build
TOP_V     := $(BUILD_DIR)/$(TOP).v

MILL      := mill

default: compile

compile:
	$(MILL) -i -j 0 rvsoc.run $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR)

bsp:
	$(MILL) -i --debug mill.bsp.BSP/install

idea:
	$(MILL) -i mill.scalalib.GenIdea/idea