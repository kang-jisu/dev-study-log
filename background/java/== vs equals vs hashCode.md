# ==, equals, hashCode


## `==`연산자
값을 비교한다. primitive type에 대해서는 값을 비교하지만, 나머지 reference type은 값이 주소값이므로 실제로 들어있는 값이 같아도 주소값이 같으면 true, 다르면 false를 반환한다.   

```java
public class equalOp{
    public static void main(){
        int a = 1;
        int b = 1;
        System.out.println(a==b); //true

        String sa = "ab"; //-> 리터럴이라서 가리키는 주소값이 같음
        String sb = "ab";

        System.out.println(sa==sb); //true

        String na = new String("ab");
        String nb = new String("ab");

        System.out.println(na==nb); //false
        System.out.println(sa==na); //false
    }
}

```

## `equals` 
equals는 Object 클래스의 메서드인데 String 클래스에서 오버라이드하여 주소값이 아닌 값으로 비교한다.

```java
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```
따라서 비교가 필요한 클래스의 equals 메소드를 오버라이드하여 주소값이 아닌 값을 비교하게 변경할 수 있다.   
-> 꼭 필요한 경우가 아니면 재정의하는것은 피해야 하며, 주의사항(규약)을 지켜야한다.  

## `hashCode()`
해싱 기법에 사용되는 해시함수를 구현한 것이다.   
해시코드가 같은 두 객체가 존재할 수는 있지만, Object의 hashCode는 객체의 주소값으로 해시 코드를 생성하므로 서로 다른 두 객체는 같은 hashCode를 가질 수 없다.   
따라서 클래스의 인스턴스 변수 값으로 객체가 같은지 판단하려면 `equals` 뿐 아니라 `hashCode()` 메서드도 오버라이딩 해주어야한다.