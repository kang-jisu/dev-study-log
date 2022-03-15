## MergeSort
분할정복 알고리즘의 하나
 - 작은 문제로 분리하고 해결한 다음, 결과를 모아서 원래의 문제를 해결하는 전략 

### 해결과정
1. 리스트 길이가 0또는 1이면 이미 정렬된 것으로 본다. 
2. 정렬되지 않은 리스트를 절반으로 잘라 두 부분으로 나눈다. 
3. 각 부분 리스트를 재귀적으로 합병정렬을 이용해 정렬한다.
4. 두 부분 리스트를 다시 하나의 정렬된 알고리즘으로 합병한다.
    - 2개의 리스트 값을 처음부터 하나씩 비교하여 두 개의 리스트 값중에서 더 작은 값을 새로운 리스트로 옮긴다. 
    - 둘 중 하나가 끝날 때 까지 되풀이한다.
    - 나머지 리스트 값들을 전부 새로운 리스트로 옮긴다.
    - 새로운 리스트를 원래 리스트로 옮긴다. 
    

### 특징

- 단점
  - 임시 배열이 필요하다. 
- 장점
  - 안정적인 정렬 방법이다.
  - 입력데이터가 뭐든 O(NlogN) 으로 동일하다. 
  - 만약 연결리스트로 구현한다면 링크 인덱스만 변경되므로 데이터 이동 비용이 작아져 효율적이다. 
    
모든 단계마다 n번 비교하는데 depth가 LogN -> NlogN


```java
public class MergeSort{
    public void merge(int[] arr, int left, int right){
        int[] tmp = new int[arr.length];
        if(left<right){
            int mid = (left+right)/2;
            int i = left;
            int j = mid+1;
            int k = left;
            while(i<=mid && j<=right) {
                if(arr[i]>arr[j]){
                    tmp[k++]=arr[j++];
                }
                else tmp[k++]=arr[i++];
            }
            while(i<=mid){
                tmp[k++]=arr[i++];
            }
            while(j<=right){
                tmp[k++]=arr[j++];
            }
            for(int idx=left;idx<=right; idx++){
                arr[idx] = tmp[idx];
            }
        }
    }
    public void mergesort(int[] arr, int left, int right){
        if(left<right){
            int mid = (left+right)/2;
            mergesort(arr, left, mid);
            mergesort(arr, mid+1, right);
            merge(arr, left, right);
        }
    }
}

```
