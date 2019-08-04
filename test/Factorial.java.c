#include "runtime/minijava.h";

typedef struct Fac_s* Fac_t;

int_t Fac_ComputeFac(Fac_t, int_t);

struct Fac_s
{
};

int_t Fac_ComputeFac(Fac_t __self, int_t __num)
{
int_t __num_aux;

boolean_t __t0;
int_t __t1, __t2;
__t1 = __num;
__t2 = 1;
__t0 = __t1 < __t2;
if (__t0) goto lbl0;
int_t __t3;
int_t __t4, __t5;
__t4 = __num;
int_t __t6;
Fac_t __t7;
__t7 = __self;
int_t __t8;
int_t __t9, __t10;
__t9 = __num;
__t10 = 1;
__t8 = __t9 - __t10;
__t6 = Fac_ComputeFac(__t7, __t8);
__t3 = __t4 * __t6;
__num_aux = __t3;
goto lbl1;
lbl0:;
int_t __t11;
__t11 = 1;
__num_aux = __t11;
lbl1:;

int_t __t12;
__t12 = __num_aux;
return __t12;
}

int main()
{
int_t __t0;
Fac_t __t1;
__t1 = malloc(sizeof(struct Fac_s));
int_t __t2;
__t2 = 10;
__t0 = Fac_ComputeFac(__t1, __t2);
printf("%d\n", __t0);
}
