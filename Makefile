BASE_DIR   := $(abspath .)
BUILD_DIR  := $(BASE_DIR)/build
MAIN_DIR   := $(BASE_DIR)/src/main/scala
TEST_DIR   := $(BASE_DIR)/src/test/scala
SCALA_FILE := $(shell find $(MAIN_DIR) -name '*.scala')
TEST_FILE  := $(shell find $(TEST_DIR) -name '*.scala')

MILL       := mill

.DEFAULT_GOAL := compile

compile: $(SCALA_FILE)
	$(MILL) -i -j 0 _.run $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR) test_run_dir

bsp:
	$(MILL) -i --debug mill.bsp.BSP/install

idea:
	$(MILL) -i mill.scalalib.GenIdea/idea

test: $(TEST_FILE)
	$(MILL) -i _.test

.PHONY: compile clean bsp idea test
