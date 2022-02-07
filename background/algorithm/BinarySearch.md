### Binary Search

**정렬되어있는** 배열에서 검색 범위를 1/2로 줄여나가며 찾아가는 방법

시간복잡도는 O(logN)



할때마다 mid,mid-1,mid+1, 부등호가 헷갈려서 한번 정리해봤다. 

---

#### 기본

`mid = (left+right)/2`에 대해서 

- mid == key 이면 종료
- mid > key 이면 right = mid-1;
- mid < key 이면 left = mid+1;

```java
public int binarSearch(int target) {
        int left = 0;
        int right = array.length-1;

        while( left <= right) {
            int mid = (left+right)/2;
            if(array[mid]==target) return mid;
            else if(array[mid] > target) right = mid -1;
            else left = mid+1;
        }
        return -1;
    }
    @Test
    public void binarySearchTest(){
        array = new int[]{1,2,3,3,4,5,6,8,10,13};
        System.out.println(binarSearch(3)); //2
        System.out.println(binarSearch(7)); //-1
        System.out.println(binarSearch(15)); //-1
        System.out.println(binarSearch(5)); //5
        System.out.println(binarSearch(1)); //0
    }

```
  
<br/>


#### lower_bound

찾고자 하는 값 **이상**이 가장 처음 나타나는 위치. 같은 원소가 여러개 있으면 가장 처음 나타나는 위치로 찾음 

```java
    public int lowerBound(int target) {
        int left=0;
        int right=array.length; // target이 array에 존재하는 값보다 클 수 있으므로
        while( left < right) {
            int mid = (left+right)/2;
            if( array[mid]< target) left = mid+1;// target보다 작을때만 left를 변경
            else right = mid;// mid값이 lowerbound가 될 수 있으므로, 그리고 최종연산후엔 항상 right이 답
        }
        return right;
    }

    @Test
    public void lowerBoundTest(){
        array = new int[]{1,2,3,3,4,5,6,8,10,13};
        System.out.println(lowerBound(3)); //2
        System.out.println(lowerBound(7)); //7
        System.out.println(lowerBound(13)); //9
        System.out.println(lowerBound(5)); //5
        System.out.println(lowerBound(1)); //0
    }
```

`array[mid] < target` 타겟보다 작을 때만 left를 증가시켜주고, 크거나 같을 때는 mid가 lowerbound가 될 수 있으므로 right=mid를 넣어준다. 

이렇게하면 left가 right보다 크거나 같아지는 순간에 right이 lower_bound가 된다. 


<br/> 

#### upper_bound

찾는 값보다 큰 값이 처음 나타나는 위치

```java
public int upperBound(int target) {
    int left=0;
    int right=array.length; // target이 array에 존재하는 값보다 클 수 있으므로
    while( left < right) {
        int mid = (left+right)/2;
        if(array[mid] <= target) left = mid+1; // target보다 큰걸 찾아야하므로  left를 증가시킬것
        else right = mid; 
    }
    return right;
}
    @Test
    public void upperBoundTest(){
        array = new int[]{1,2,3,3,4,5,6,8,10,13};
        System.out.println(upperBound(3));//4
        System.out.println(upperBound(7));//7
        System.out.println(upperBound(15));//10
        System.out.println(upperBound(5));//6
        System.out.println(upperBound(1));//1
    }
```

`array[mid]<=target` 같은거 나오면 그거보다 큰걸 찾아야되니까 증가시켜줌

target보다 큰게 나오면 right=mid; mid가 upper_bound가 될 수 있으니까 mid-1하면 안됨.

left>=right이 되면 탐색 종료 . 

