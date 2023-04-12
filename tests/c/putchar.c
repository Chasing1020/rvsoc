static void ebreak(int arg0, int arg1) {
  asm volatile("addi a0, x0, %0;"
               "addi a1, x0, %1;"
               "ebreak" : : "i"(arg0), "i"(arg1));
}

static int putchar(char ch) {
  ebreak(0, ch);
  return ch;
}

static void exit() {
  asm volatile("unimp");
}

// TODO: fix error: impossible constraint in 'asm'
void _start() {
  char* s = "Hello, world!";
  for (int i = 0; i < 12; i++)
    putchar(s[i]);
  exit();
}