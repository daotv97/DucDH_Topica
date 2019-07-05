# Dining Philosophers ( Bữa tối của các triết gia )

-----------
[![](https://sphof.readthedocs.io/_images/philtable.png)](https://sphof.readthedocs.io/_images/philtable.png)

Có năm nhà triết gia, vừa suy nghĩ vừa ăn tối. Các triết gia ngồi trên một bàn hình tròn, trước mặt họ là các đĩa thức ăn, mỗi người một đĩa. Có 5 chiếc đũa được đặt xen kẽ giữa các triết gia. Như hình vẽ trên:

### 1. Phát biểu bài toán.

**Bài toán được phát biểu** : "5 nhà triết học cùng ngồi ăn tối với món spaghetti nổi tiếng. Mỗi nhà triết học cần dùng 2 cái nĩa để có thể ăn spaghetti . Nhưng trên bàn chỉ có tổng cộng 5 cái nĩa để xen kẽ với 5 cái đĩa. Mỗi nhà triết học sẽ suy ngẫm các triết lý của mình đến khi cảm thấy đói thì dự định lần lượt cầm 1 cái nĩa bên trái và 1 cái nĩa bên phải để ăn. Nếu cả 5 nhà triết học đều cầm cái nĩa bên trái cùng lúc, thì sẽ không có ai có được cái nĩa bên phải để có thể bắt đầu thưởng thức spaghetti". 
Tìm cách để không ai phải chết đói là vấn đề của bài toán.

------------

### 2. Mô tả bài toán.

Mỗi triết gia sẽ tượng trưng cho một tiến trình, và những chiếc đũa là tài nguyên của hệ thống. Các triết gia sẽ có 4 trạng thái lần lượt theo thứ tự là:

1. **Suy nghĩ** ( Thinking )
2. **Lấy đũa** ( Take Choptisks )
3. **Đang ăn** ( Eating )
4. **Thả đũa** ( Put Choptisks )

`Chú ý: Khi lấy được 2 chiếc đũa từ hai láng giềng gần nhất thì triết gia mới có thể ăn`

------------


### 3. Giải pháp bài toán.

Bài toán các triết gia ăn tối được xem như một bài toán đồng bộ hoá kinh điển. Nó trình bày yêu cầu cấp phát nhiều tài nguyên giữa các quá trình trong cách tránh việc khoá chết và đói tài nguyên.

Một giải pháp đơn giản là thể hiện mỗi chiếc đũa bởi một **biến semaphore**. Một triết gia cố gắng chiếm lấy một chiếc đũa bằng cách thực thi thao tác wait trên biến semaphore đó; triết gia đặt hai chiếc đũa xuống bằng cách thực thi thao tác signal trên các biến semaphore tương ứng. Do đó, dữ liệu được chia sẻ là:

```java
semaphore chopstick[5];
```
Ở đây tất cả các phần tử của chopstick được khởi tạo 1. Cấu trúc của philosopher i được hiển thị:

```java
do {
    wait(chopstick[i]);
    wait(chopstick[(i + 1) % 5]);…
    ăn…;
	signal(chopstick[i]);
    signal(chopstick[(i + 1) % 5]);…
    suy nghĩ…;
} while (1);
```
Mặc dù giải pháp này đảm bảo rằng không có hai láng giềng nào đang ăn cùng một lúc nhưng nó có khả năng gây ra **khoá chết** (Deadlock). Giả sử rằng năm triết gia bị đói cùng một lúc và mỗi triết gia chiếm lấy chiếc đũa bên trái của ông ta. Bây giờ tất cả các phần tử chopstick sẽ là 0. Khi mỗi triết gia cố gắng dành lấy chiếc đũa bên phải, triết gia sẽ bị chờ mãi mãi.

Nhiều giải pháp khả thi đối với vấn đề khoá chết được liệt kê tiếp theo. Giải pháp cho vấn đề các triết gia ăn tối mà nó đảm bảo không bị khoá chết.

- ***Cho phép nhiều nhất bốn triết gia đang ngồi cùng một lúc trên bàn**.*

- ***Cho phép một triết gia lấy chiếc đũa của ông ta chỉ nếu cả hai chiếc đũa là sẳn dùng (để làm điều này ông ta phải lấy chúng trong miền tương trục).***

- ***Dùng một giải pháp bất đối xứng; nghĩa là một triết gia lẽ chọn đũa bên trái đầu* *tiên của ông ta và sau đó đũa bên phải, trái lại một triết gia chẳn chọn chiếc đũa bên phải và sau đó chiếc đũa bên phải của ông ta.***

