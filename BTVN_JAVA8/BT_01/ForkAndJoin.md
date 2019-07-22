# Tìm hiều về Fork/ Join.

------------
## Giới thiệu Fork/ Join

**Fork/ Join** là một mô hình xử lý song song, nó cung cấp các công cụ giúp tăng tốc xử lý song song bằng cách cố gắng sử dụng tất cả các lõi bộ xử lý có sẵn, được thực hiện thông qua cách tiếp cận phân chia **(fork)** và gộp **(join) ** task. Mục đích là để sử dụng tất cả các khả năng xử lý để nâng cao hiệu suất cho các ứng dụng.

**- FORK **: Trong thực tế, bước đầu tiên framework Fork/ Join thực hiện là chia nhỏ task (fork/ split), đệ quy chia nhỏ nhiệm vụ thành các nhiệm vụ phụ nhỏ hơn cho đến khi chúng đơn giản đủ để được thực hiện xử lý không đồng bộ.

	Bằng cách chia nhỏ thành các nhiệm vụ con, mỗi nhiệm vụ con có thể được thực hiện song song bởi các CPU khác nhau, hoặc các luồng khác nhau trên cùng một CPU.
	Một nhiệm vụ chỉ phân chia thành các nhiệm vụ phụ nếu công việc mà nhiệm vụ được đưa ra là đủ lớn để điều này có ý nghĩa. Có một chi phí để chia tách một nhiệm vụ thành các nhiệm vụ phụ, vì vậy với số lượng nhỏ công việc trên không thể lớn hơn tốc độ đạt được bằng cách thực hiện các công việc phụ đồng thời.

[![](https://gpcoder.com/wp-content/uploads/2018/03/java-fork-and-join-1.png)](https://gpcoder.com/wp-content/uploads/2018/03/java-fork-and-join-1.png)

**- JOIN**: Sau đó, phần gộp kết quả (join) bắt đầu, trong đó các kết quả của tất cả các nhiệm vụ phụ được đệ quy một cách đệ quy vào một kết quả, hoặc trong trường hợp một nhiệm vụ trả về void, chương trình chỉ cần đợi cho đến khi mỗi nhiệm vụ phụ được thực hiện.
Có thể mô tả code như sau:

	Khi một nhiệm vụ (parent task) đã tự tách mình thành các nhiệm vụ con (sub task), nhiệm vụ cha sẽ đợi cho đến khi các nhiệm vụ con hoàn thành.
	
[![](https://gpcoder.com/wp-content/uploads/2018/03/java-fork-and-join-2.png)](https://gpcoder.com/wp-content/uploads/2018/03/java-fork-and-join-2.png)

**Ví dụ**:

    if (tác vụ nhỏ) {
     
                    Trực tiếp giải quyết/ trả kết quả
     
    } else {
     
                    Chia nhỏ/ Tách tác vụ thành các tác vụ con
     
                    Đệ quy giải quyết từng tác vụ con (fork)
     
                    Tổng hợp kết quả (join)
     
    }
