# Deadlock

-----------
### 1. Tìm hiểu về deadlock.
Theo như docs từ **Oracle** thì: <br />
`Deadlock describes a situation where two or more threads are blocked forever, waiting for each other.` <br />

Tạm dịch có nghĩa là deadlock mô tả trạng thái khi mà 2 hoặc nhiều thread bị khóa vĩnh viễn, chờ đợi lẫn nhau. Nói rõ hơn thì: 
- Deadlock xảy ra trong môi trường **multi-thread** (Tất nhiên vì nếu có 1 thread thì chẳng cần phải đợi thằng thread nào nữa, thích thằng nào thì gọi thằng đó trả lời ngay lập tức).


- Khi có **thread (1)** đang giữ tài nguyên (A) và cần truy cập tài nguyên(B) để tiếp tục xử lý (call method xử lý…) nhưng tài nguyên (B) đang được **thread (2)** sử dụng, trong lúc này thì **thread (2)** cũng đang cần truy cập vào tài nguyên (A) mà **thread (1)** đang sử dụng. Lúc này thì cả 2 thread đều không thể tiếp tục thực hiện mà đều phải “chờ” nhau nhưng không biết khi nào thì sẽ kết thúc.

-  **Deadlock** xảy ra chúng ta sử dụng **synchronization** với mục đích đảm bảo **thread-safe** nhưng nếu không cẩn thận khi sử dụng **lock-object** (objectidentifier) thì sẽ dễ dẫn đến deadlock.

Khi sử dụng **synchronization**, thread sẽ chiếm giữ lock object để đảm bảo tại 1 thời điểm, chỉ có 1 thread được thực thi đoạn block code. Vấn đề nảy sinh khi 2 hoặc nhiều thread giữ block object mà thread kia đang cần, dẫn đến tình trạng lock lẫn nhau.

[![](http://thachleblog.com/wp-content/uploads/2016/10/java-thread-deadlock-crunchify-tutorial.jpg)](http://thachleblog.com/wp-content/uploads/2016/10/java-thread-deadlock-crunchify-tutorial.jpg)

##### Để làm rõ hơn về deadlock thì chúng ta sẽ tìm hiểu ví dụ ở dưới sau đây.
------------

### 2. Ví dụ về deadlock trong Java.
Hình dưới là cảnh một cảnh sát đang nắm giữ một tên cướp. Anh cảnh sát muốn tên cướp còn lại phải trao trả con tin trước thì ảnh mới thả tên cướp đang nắm giữ. Trong khi đó, tên cướp kia thì nhất định không trả con tin, buộc anh cảnh sát phải thả đồng bọn của hắn ra trước.

[![](https://danlaptrinh.files.wordpress.com/2017/11/3xvzk.png)](https://danlaptrinh.files.wordpress.com/2017/11/3xvzk.png)

Vậy là, mỗi phe trong tình huống này đều nắm giữ riêng con tin của họ, và không phe nào chịu trao trả con tin về cho phe kia cả. Tình huống này rõ ràng là sẽ khó có một thỏa hiệp đạt được trong một thời gian ngắn. **Deadlock khi này đã xảy ra**.

#### Mô tả rõ hơn bằng code trong java tình huống trên:
```java
public static Object Lock1 = new Object();
public static Object Lock2 = new Object();

public static void main(String args[]) {
    Thread1 T1 = new Thread1();
    Thread2 T2 = new Thread2();
    T1.start();
    T2.start();
}

private static class Thread1 extends Thread {
    public void run() {
        synchronized(Lock1) {
            System.out.println("Cop: Holding criminal's friend...");

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
            System.out.println("Cop: Waiting for criminal release hostage...");

            synchronized(Lock2) {
                System.out.println("Cop 1: Holding criminal's friend...");
            }
        }
    }
}
private static class Thread2 extends Thread {
    public void run() {
        synchronized(Lock2) {
            System.out.println("Criminal: Holding hostage...");

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
            System.out.println("Criminal: Waiting for cop release friend...");
            synchronized(Lock1) {
                System.out.println("Criminal: Holding friend...");
            }
        }
    }
}
```

### Output
	Cop: Holding criminal's friend...
	Criminal: Holding hostage...
	Cop: Waiting for criminal release hostage...
	Criminal: Waiting for cop release friend...

Tình hình có vẻ căng thẳng nhỉ! Cả 2 đều đang chờ nhau và không bên nào chịu thả con tin hay nói cách khác là mỗi “Lock” cần sử dụng đều đang bị thread khác chiếm giữ.

**Để giải quyết trường hợp này. Chúng ta có 2 cách:**

1. Chỉ sử dụng 1 lock-object (objectidentifier) để synchronized tất cả các đoạn hoặc block code cần synchronization. (Trong ví dụ trên thì chỉ sử dụng Lock1 hoặc Lock2.  Nếu có thể thì không nên sử dụng cùng lúc 2 lock-object)

2. Sử dụng lock-object (objectidentifier) theo thứ tự giống nhau với các block synchorined. Khi các lock-object được sử dụng để synchronization theo thứ tự thì sẽ ngăn được tình trạng các thread giữ nhiều “lock” khi thực hiện mà không cho phép thread khác sử dụng. (Tương tự ví dụ trên thì Thread1 synchronized với Lock1 & Lock thì Thread2 cũng synchronized với thứ tự Lock1 & Lock2)


