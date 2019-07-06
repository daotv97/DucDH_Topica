# Thread Pool

------------

### 1. Giới thiệu ThreadPool.
Xét về hiệu suất, tạo ra một **Thread** mới là một hoạt động tốn kém bởi vì nó đòi hỏi chương trình phải cung cấp tài nguyên để có thể thực thi các tác vụ. Trên thực tế, ** ThreadPool** được sử dụng cho các ứng dụng quy mô lớn, nó tác động tới hiệu năng của các chương trình lớn, đòi hỏi xử lý đồng thời nhiều tác vụ cùng một lúc.

Trong Java, **ThreadPool** được dùng để giới hạn số lượng **Thread** được chạy bên trong ứng dụng của chúng ta trong cùng một thời điểm. Nếu chúng ta không có sự giới hạn này, mỗi khi có một **Thread** mới được tạo ra và được cấp phát bộ nhớ bằng từ khóa **new** thì sẽ có vấn đề về bộ nhớ và hiệu suất, có thể dẫn đến lỗi crash chương trình.

[![](https://batnamv.files.wordpress.com/2017/04/capture2.png?w=344&h=184)](https://batnamv.files.wordpress.com/2017/04/capture2.png?w=344&h=184)


------------



### 2. Ví dụ.

Bây giờ, giả sử chúng ta có một Server Web. Nếu chúng ta nhận 1 request từ client, chúng ta sẽ xử lý mất 0.5s và trả về kết quả cho người dùng. Thế nếu có 2 người request cùng lúc thì sao? => giải quyết bằng cách mỗi một request sẽ xử lý ở 1 thread, đơn giản.
Thế nếu có 100 người request cùng lúc ??? => mỗi người tạo một thread... **wait a minute**.... ( nếu 1 tháng có 10M lượt request => tạo ra 10M thread) .Nếu chúng ta tạo 1-2 thread mới, chả ai trách gì chúng ta cả. Nhưng nếu  tạo liên tục và tới hàng trăm cái mới mỗi lần nhưng lại giải quyết cùng 1 vấn đề thì có lỗ hổng đấy. Vì chi phí của việc tạo 1 thread là tương đối lớn, thường dẫn tới các vấn đề về hiệu năng và cấp phát dữ liệu.

Với việc xử lý các tác vụ liên tục như vậy, có một giải pháp là sử dụng **Thread Pool**.

Ở ví dụ trên, Bây giờ chúng ta sẽ chỉ sử dụng 30 thread thôi! Và đặt 30 thread này ở trạng thái không làm gì và vứt vào 1 cái Pool (1 cái bể chứa, kiểu vậy). Với mỗi request đến, chúng ta sẽ lấy trong Pool ra 1 thread và xử lý công việc, xử lý xong, thì cất thread vào ngược trở lại pool. Đơn giản vậy thôi, như thế chúng ta sẽ không phải tạo mới Thread nữa. Tránh tình tốn chi phí và hiệu năng.

Vấn đề là giả sử có hơn 31 request tới cùng lúc thì sao? rất đúng, trường hợp này là chắc chắn có. Lúc này Pool sẽ không còn thread nào sẵn có nữa. Nên 1 request còn lại sẽ bị đẩy vào 1 hàng đợi BlockingQueue. Nó sẽ đợi ở đó, bao giờ Pool có 1 thread rảnh rỗi thì sẽ quay lại xử lý nốt .


[![](https://batnamv.files.wordpress.com/2017/04/capture11.png)](https://batnamv.files.wordpress.com/2017/04/capture11.png)

------------


### 3. Một số loại ThreadPool do Concurrency API cung cấp.

#### Java Concurrency API hỗ trợ một vài loại ThreadPool sau:

- **Cached thread pool**: Mỗi nhiệm vụ sẽ tạo ra thread mới nếu cần, nhưng sẽ tái sử dụng lại các thread cũ. (Cái này nên áp dụng với các task nhỏ, tốn ít tính toán)

- **Fixed thread pool**: Giới hạn số lượng tối đa của các Thread được tạo ra. Các task khác đến sau phải chờ trong hàng đợi (BlockingQueue).

- **Single-threaded pool**: chỉ giữ một Thread thực thi một nhiệm vụ một lúc.

- ** Fork/Join pool**: một Thread đặc biệt sử dụng Fork/ Join Framework bằng cách tự động chia nhỏ công việc tính toán cho các core xử lý. (Tính toán song song).

Trong thực tế, **ThreadPool** được sử dụng rộng rãi trong các máy chủ web, nơi một ThreadPool được sử dụng để phục vụ các yêu cầu của khách hàng. Thread pool cũng được sử dụng trong các ứng dụng cơ sở dữ liệu nơi mà một ThreadPool được sử dụng để duy trì các kết nối mở với cơ sở dữ liệu.

Việc cài đặt **ThreadPool** là một công việc phức tạp, nhưng chúng ta không cần phải lo lắng điều này bởi vì Java Concurrency API đã xây dựng sẵn (build-in) các lớp hỗ trợ ThreadPool trong gói **java.util.concurrent.**


------------

### 4. Executor trong ThreadPool.

[![](https://gpcoder.com/wp-content/uploads/2018/02/threadpool-executor-service.png)](https://gpcoder.com/wp-content/uploads/2018/02/threadpool-executor-service.png)

**Executor** là một class đi kèm trong gói java.util.concurrent, là một đối tượng chịu trách nhiệm quản lý các luồng và thực hiện các tác vụ Runnable được yêu cầu xử lý. Nó tách riêng các chi tiết của việc tạo Thread, lập kế hoạch (scheduling), … để chúng ta có thể tập trung phát triển logic của tác vụ mà không quan tâm đến các chi tiết quản lý Thread.

#### **Chúng có thể tạo một Executor bằng cách sử dụng một trong các phương thức được cung cấp bởi lớp tiện ích Executors như sau:**

- **newSingleThreadExecutor()**: trong ThreadPool chỉ có 1 Thread và các task (nhiệm vụ) sẽ được xử lý một cách tuần tự.

- **newCachedThreadPool()**: như giải thích ở trên, nó sẽ có 1 số lượng nhất định thread để sử dụng lại, nhưng vẫn sẽ tạo mới thread nếu cần. Mặc định nếu một Thread không được sử dụng trong vòng 60 giây thì Thread đó sẽ bị tắt.

- **newFixedThreadPool(int n)**: trong Pool chỉ có n Thread để xử lý nhiệm vụ, các yêu cầu tới sau bị đẩy vào hàng đợi.

- **newScheduledThreadPool(int corePoolSize)**: tương tự như newCachedThreadPool() nhưng sẽ có thời gian delay giữa các Thread.

- **newSingleThreadScheduledExecutor()**: tương tự như newSingleThreadExecutor() nhưng sẽ có khoảng thời gian delay giữa các Thread.

### 5. Java code.

[Custom threadpool by DucDH](https://github.com/huyductopica/DucDH_Topica/tree/master/BTVN_04/BT2 "Custom threadpool")




