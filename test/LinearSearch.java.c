#include "runtime/minijava.h";

typedef struct LS_s* LS_t;

int_t LS_Start(LS_t, int_t);
int_t LS_Search(LS_t, int_t);
int_t LS_Print(LS_t);
int_t LS_Init(LS_t, int_t);

struct LS_s
{
IntArray_t __number;
int_t __size;
};

int_t LS_Start(LS_t __self, int_t __sz)
{
int_t __aux01;
int_t __aux02;

int_t __t0;
int_t __t1;
LS_t __t2;
__t2 = __self;
int_t __t3;
__t3 = __sz;
__t1 = LS_Init(__t2, __t3);
__aux01 = __t1;
int_t __t4;
int_t __t5;
LS_t __t6;
__t6 = __self;
__t5 = LS_Print(__t6);
__aux02 = __t5;
__NULL = 9999;
printf("%d\n", __NULL);
int_t __t7;
LS_t __t8;
__t8 = __self;
int_t __t9;
__t9 = 8;
__t7 = LS_Search(__t8, __t9);
printf("%d\n", __t7);
int_t __t10;
LS_t __t11;
__t11 = __self;
int_t __t12;
__t12 = 12;
__t10 = LS_Search(__t11, __t12);
printf("%d\n", __t10);
int_t __t13;
LS_t __t14;
__t14 = __self;
int_t __t15;
__t15 = 17;
__t13 = LS_Search(__t14, __t15);
printf("%d\n", __t13);
int_t __t16;
LS_t __t17;
__t17 = __self;
int_t __t18;
__t18 = 50;
__t16 = LS_Search(__t17, __t18);
printf("%d\n", __t16);

int_t __t19;
__t19 = 55;
return __t19;
}

int_t LS_Print(LS_t __self)
{
int_t __j;

int_t __t20;
__t20 = 1;
__j = __t20;

int_t __t21;
__t21 = 0;
return __t21;
}

int_t LS_Search(LS_t __self, int_t __num)
{
int_t __j;
boolean_t __ls01;
int_t __ifound;
int_t __aux01;
int_t __aux02;
int_t __nt;

int_t __t22;
__t22 = 1;
__j = __t22;
boolean_t __t23;
__t23 = 0;
__ls01 = __t23;
int_t __t24;
__t24 = 0;
__ifound = __t24;

int_t __t25;
__t25 = __ifound;
return __t25;
}

int_t LS_Init(LS_t __self, int_t __sz)
{
int_t __j;
int_t __k;
int_t __aux01;
int_t __aux02;

int_t __t26;
__t26 = __sz;
__size = __t26;
IntArray_t __t27;
__number = __t27;
int_t __t28;
__t28 = 1;
__j = __t28;
int_t __t29;
int_t __t30, __t31;
__t30 = __size;
__t31 = 1;
__t29 = __t30 + __t31;
__k = __t29;

int_t __t32;
__t32 = 0;
return __t32;
}

int main()
{
int_t __t0;
LS_t __t1;
__t1 = malloc(sizeof(struct LS_s));
int_t __t2;
__t2 = 10;
__t0 = LS_Start(__t1, __t2);
printf("%d\n", __t0);
}
