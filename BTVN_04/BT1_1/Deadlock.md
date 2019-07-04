# Deadlock

-----------
### 1. Tìm hiểu về deadlock.
Theo như docs từ **Oracle** thì: <br />
`Deadlock describes a situation where two or more threads are blocked forever, waiting for each other.` <br />

Tạm dịch có nghĩa là deadlock mô tả một tình huống khi mà 2 hoặc nhiều thread bị blvĩnh viễn, chờ đợi lẫn nhau. Nói rõ hơn thì: 
- Deadlock xảy ra trong môi trường **multi-thread** (Tất nhiên vì nếu có 1 thread thì chẳng cần phải đợi thằng thread nào nữa, thích thằng nào thì gọi thằng đó trả lời ngay lập tức).


- Khi có **thread (1)** đang giữ tài nguyên (A) và cần truy cập tài nguyên(B) để tiếp tục xử lý (call method xử lý…) nhưng tài nguyên (B) đang được **thread (2)** sử dụng, trong lúc này thì **thread (2)** cũng đang cần truy cập vào tài nguyên (A) mà **thread (1)** đang sử dụng. Lúc này thì cả 2 thread đều không thể tiếp tục thực hiện mà đều phải “chờ” nhau nhưng không biết khi nào thì sẽ kết thúc.

-  **Deadlock** xảy ra chúng ta sử dụng **synchronization** với mục đích đảm bảo **thread-safe** nhưng nếu không cẩn thận khi sử dụng **lock-object** (objectidentifier) thì sẽ dễ dẫn đến deadlock.

Khi sử dụng **synchronization**, thread sẽ chiếm giữ lock object để đảm bảo tại 1 thời điểm, chỉ có 1 thread được thực thi đoạn block code. Vấn đề nảy sinh khi 2 hoặc nhiều thread giữ block object mà thread kia đang cần, dẫn đến tình trạng lock lẫn nhau.

[![](http://thachleblog.com/wp-content/uploads/2016/10/java-thread-deadlock-crunchify-tutorial.jpg)](http://thachleblog.com/wp-content/uploads/2016/10/java-thread-deadlock-crunchify-tutorial.jpg)

##### Để làm rõ hơn về deadlock thì chúng ta sẽ tìm hiểu ví dụ ở dưới sau đây.
------------

### 2. Ví dụ về deadlock trong Java.
Chúng ta vẫn sẽ đến với kịch bản xây dựng ứng dụng ngân hàng. Giả sử hôm nay sếp ngân hàng đến nói với dev rằng hãy xây dựng thêm chức năng chuyển khoản giữa các tài khoản với nhau. Sau khi chức năng xây dựng xong, ở một gia đình nọ có hai vợ chồng mỗi người đều có 1 tài khoản riêng tại ngân hàng. Một ngày đẹp trời do không hiểu ý nhau, anh chồng vô tài khoản của ảnh chuyển cho cô vợ 3 triệu VND, đồng thời cùng lúc đó, cô vợ cũng vô tài khoản của cổ chuyển cho anh chồng 2 triệu VND. Vấn đề trớ trêu là 2 người này cùng gần như thực hiện đồng thời lệnh chuyển tiền. Và một điều kỳ lạ đã xảy ra, ứng dụng bị treo, có nghĩa là 2 vợ chồng họ đợi hoài mà lệnh chuyển tiền vẫn không thành công. 

Giả sử lớp BankAccount có sẵn các phương thức rút (withdraw) và nạp (deposit).

```java
public class BankAccount extends Object {
     
    long amount = 5000000; // Số tiền có trong tài khoản
    String accountName = "";
     
    public BankAccount(String accountName) {
        this.accountName = accountName;
    }
 
    public synchronized void withdraw(long withdrawAmount) {
        // In ra trạng thái bắt đầu trừ tiền
        System.out.println(accountName + " withdrawing...");
         
        // Trừ tiền
        amount -= withdrawAmount;
    }
     
    public synchronized void deposit(long depositAmount) {
        // In ra trạng thái bắt đầu nạp tiền
        System.out.println(accountName + " depositting...");
         
        // Nạp tiền
        amount += depositAmount;
    }
     
	// Chuyển tiền
    public void transferTo(BankAccount toAccount, long transferAmount) {
        synchronized(this) {
            // Rút tiền từ tài khoản này
            this.withdraw(transferAmount);
             
            synchronized(toAccount) {
                // Nạp tiền vào toAccount
                toAccount.deposit(transferAmount);
            }
             
            // In số dư tài khoản khi kết thúc quá trình chuyển tiền
            System.out.println("The amount of " + accountName + " is: " + amount);
        }
    }
}
```
#### Ở phương thức main() chỉ việc gọi các lệnh chuyển khoản như sau.

```java
public static void main(String[] args) {
    // Khai báo tài khoản của anh chồng và cô vợ riêng
    BankAccount husbandAccount = new BankAccount("Husband's Account");
    BankAccount wifeAccount = new BankAccount("Wife's Account");
 
    // Anh chồng muốn chuyển 3 triệu từ tài khoản của ảnh qua tài khoản cô vợ
    Thread husbandThread = new Thread() {
        @Override
        public void run() {
            husbandAccount.transferTo(wifeAccount, 3000000);
        }
    };
 
    // Cô vợ muốn chuyển 2 triệu từ tài khoản của cổ qua tài khoản của anh chồng
    Thread wifeThread = new Thread() {
        @Override
        public void run() {
            wifeAccount.transferTo(husbandAccount, 2000000);
        }
    };
 
    // Hai người thực hiện lệnh chuyển tiền gần như đồng thời
    husbandThread.start();
    wifeThread.start();
}
```
### Output
	Husband's Account withdrawing...
	Wife's Account withdrawing...

Cả 2 Thread khi được khởi chạy, chỉ làm được mỗi thao tác trừ tiền của chính tài khoản nguồn. Còn sau đó đến phương thức nạp tiền cho tài khoản đích thì… không thể gọi đến được. Ứng dụng lúc này vẫn đang chạy. Cái sự ứng dụng mãi mãi không thể kết thúc được là vì khi này bản thân mỗi Thread khi được khởi tạo đã giữ lấy Lock trên Monitor của một tài khoản, các Thread khác không thể can thiệp vào tài khoản mà mỗi Thread đang giữ được. Việc mỗi Thread đều giữ một tài khoản và chờ đến lượt sử dụng tài khoản khác (cũng đang bị giữ bởi một Thread khác) như vậy được gọi là Deadlock.


