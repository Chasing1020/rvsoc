static void ebreak(int arg0, int arg1) {
  asm volatile("mv a0, %0;"
               "mv a1, %1;"
               "ebreak" :: "r"(arg0), "r"(arg1));
}

static int putchar(char ch) {
  ebreak(0, ch);
  return ch;
}

static void exit() {
  asm volatile("unimp");
}

void _start() {
  char* s = "Hello";
  for (int i = 0; i < 5; i++)
    putchar(s[i]);
  exit();
}