# RISC-V SoC with Chisel3

This project contains a complete RISC-V SoC implemented using Chisel3. The SoC supports features such as AXI4-Lite,
caching, bus, and so on. This README will provide instructions for building and running the SoC.

## Prerequisites

Before you can build and run the project, you will need to make sure you have the following software installed:

<details>
  <summary>
    Verilator, RISC-V GNU toolchain, Mill, Scala
  </summary>

```shell
sudo pacman -S verilator riscv64-unknown-elf-binutils riscv64-unknown-elf-gcc riscv64-unknown-elf-newlib mill scala 
```

```shell
brew install verilator riscv-tools mill scala@2.13
```
</details>

## Building the SoC

To build the Project, please follow these steps:

```shell
# Clone the repository
git clone https://github.com/Chasing1020/rvsoc.git
# Change into the project directory
cd rvsoc
# This will compile the Chisel code and generate Verilog files
make
# or you just want test the Modules 
make test
```

## Conclusion 

Congratulations! You now have a complete RISC-V SoC implemented using Chisel3. 

You can use this SoC as a starting point for your own projects, or modify it to suit your needs. 

If you have any questions or issues, please feel free to open an issue in the repository.

## License

RVSoC is under the Apache 2.0 license. See the [LICENSE](./LICENSE) file for details.
