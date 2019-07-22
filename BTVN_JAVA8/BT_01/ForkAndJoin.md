# Tìm hiều về Fork/ Join.

------------
## 1. Giới thiệu Fork/ Join

**Fork/ Join** là một mô hình xử lý song song, nó cung cấp các công cụ giúp tăng tốc xử lý song song bằng cách cố gắng sử dụng tất cả các lõi bộ xử lý có sẵn, được thực hiện thông qua cách tiếp cận phân chia **(fork)** và gộp **(join) ** task. Mục đích là để sử dụng tất cả các khả năng xử lý để nâng cao hiệu suất cho các ứng dụng. 
-Ý tưởng khá đơn giản: một nhiệm vụ lớn hơn có thể được chia thành các nhiệm vụ nhỏ hơn mà các giải pháp sau đó có thể được kết hợp. Miễn là các tác vụ nhỏ hơn là độc lập, chúng có thể được thực thi song song.

------------



**Ví dụ: tìm giá trị lớn nhất trong một mảng lớn các số nguyên. Mỗi tác vụ được cung cấp ba dữ liệu:**
	- Cho 1 mảng dữ liệu sẵn.
	- Chỉ số bắt đầu và kết thúc để tìm kiếm.

Việc tính toán bắt đầu bằng việc tạo ra một "nhiệm vụ gốc", đại diện cho vấn đề tổng thể.

    rootTask = new Task(arr, 0, arr.length)

Giả sử rằng lớp nhiệm vụ có một phương thức tính toán tìm kiếm phạm vi đã chỉ định. Phương thức tính toán sẽ kiểm tra xem có bao nhiêu phần tử trong phạm vi cần tìm kiếm. Nếu số lượng phần tử trong phạm vi dưới một giá trị ngưỡng, thì việc tính toán tuần tự được thực hiện. Mặt khác, phạm vi được chia thành hai phạm vi con chứa một nửa các phần tử. Nhiệm vụ phụ được tạo để tìm kiếm các phạm vi phụ. Giá trị tối đa của các giá trị tối đa trong các phần phụ là giá trị tối đa trong phạm vi tổng thể.


**Pseudocode**:

    // returns maximum integer in range
    int compute() {
            if ((end - start) <= THRESHOLD) {
                    return result of sequential search for maximum element in range
            }
    
            // find middle index in range
            int mid = start + (end - start) / 2
    
            // create subtasks
            left = new Task(array, start, mid)
            right = new Task(array, mid, end)
    
            // start parallel tasks
            left.fork()
            right.fork()
    
            // wait for parallel tasks to complete, and get their results
            int leftMax = left.join()
            int rightMax = right.join()
    
            // max of sub-ranges is max of overall range
            return maximum of leftMax, rightMax
    }

Lưu ý rằng các đối tượng tác vụ được coi như các thread.

------------

## 2. Chọn một ngưỡng

Một trong những điều chính cần xem xét khi thực hiện thuật toán sử dụng song song **fork / join** là chọn ngưỡng xác định rằng một tác vụ sẽ thực hiện tính toán tuần tự chứ không phải thực hiện các tác vụ phụ song song.

- Nếu ngưỡng quá lớn, thì chương trình có thể không tạo đủ tác vụ để tận dụng tối đa các bộ xử lý / lõi có sẵn.

- Nếu ngưỡng quá nhỏ, thì chi phí quản lý và tạo nhiệm vụ có thể trở nên quan trọng.

Nói chung, một số thử nghiệm sẽ là cần thiết để tìm một giá trị ngưỡng thích hợp.

Task != Thread, Work stealing

------------



## 3. Task != Thread, Work stealing

Work stealing là cơ chế giúp scheduler (có thể là trên ngôn ngữ, hoặc OS) có thể thực hiện việc tạo thên M thread mới hoạt động mượt mà trên N core, với M có thể lớn hơn N rất nhiều.

Idea của work-stealing scheduler là mỗi một core sẽ có một queue những task phải làm. Mỗi task đó bao gồm một list các instructions phải thực hiện một cách tuần tự. Khi một processor làm hết việc của mình, nó sẽ nhìn ngó sang các processor xung quanh, xem có gì cần làm không và “steal” công việc từ đó.

Một mô hình khác với work stealing là work sharing, tức là mỗi task sẽ quyết fix là sẽ được thực hiện trên processor nào.

Mặc dù các tác vụ hoạt động rất giống các luồng, chúng tôi không muốn thực hiện sau đó tương đương với các luồng. Các tác vụ "nhẹ" hơn nhiều so với các luồng và nói chung, một chương trình fork / tham gia nên có hiệu quả hợp lý ngay cả khi một số lượng lớn các tác vụ được tạo. Vì lý do này, việc phân bổ một luồng cho mỗi tác vụ không phải là một chiến lược tốt, bởi vì chi phí tạo, chạy và kết thúc các luồng có thể tạo ra chi phí làm giảm tốc độ song song có thể.

- *Trong thực tế, các khung **fork / join** sử dụng một nhóm các thread trong đó một số thread cố định được tạo. Mỗi thread có một hàng các nhiệm vụ đang chờ để thực thi. Khi một tác vụ được bắt đầu (rẽ nhánh), nó sẽ được thêm vào hàng đợi của luồng đang thực thi tác vụ chính của nó,*

- *Bởi vì mỗi thread chỉ có thể thực thi một nhiệm vụ tại một thời điểm, mỗi hàng đợi nhiệm vụ của mỗi thread có thể tích lũy các tác vụ hiện không thực thi. Các luồng không có nhiệm vụ được phân bổ cho chúng sẽ cố gắng đánh cắp một tác vụ từ một thread có trong hàng đợi khi nó có ít nhất một tác vụ. đây được gọi là đánh cắp công việc. Theo cơ chế này, các tác vụ được phân phối cho tất cả các thread trong nhóm các threads.*

- *Bằng cách sử dụng nhóm các threads với việc đánh cắp khung **fork / join** có thể cho phép phân chia vấn đề tương đối tốt, nhưng chỉ tạo ra số lượng luồng tối thiểu cần thiết để khai thác triệt để các lõi CPU có sẵn. Thông thường, nhóm luồng sẽ có một luồng trên mỗi lõi CPU có sẵn.*

## 4. Nguyên tắc hoạt động.

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

------------


**
Source code demo tìm giá trị lớn nhất của một phần tử dử dụng fork/join**

    public class FindMax {
    	//
    	// Threshold value: when a range of the array contains more than
    	// this many elements, we will fork tasks to divide the array
    	// into smaller ranges.
    	//
    	private static final int THRESHOLD = 100000;
    	
    	//
    	// Task class.  Represents a range of an array to be searched
    	// to find the maximum value.
    	//
    	static class FindMaxTask extends RecursiveTask<Integer> {
    		private static final long serialVersionUID = 1L;
    
    		private int[] arr;
    		private int start, end;
    		
    		public FindMaxTask(int[] arr, int start, int end) {
    			this.arr = arr;
    			this.start = start;
    			this.end = end;
    		}
    		
    		@Override
    		protected Integer compute() {
    			if (end - start <= THRESHOLD) {
    				// number of elements is at or below the threshold - compute directly
    				return computeDirectly();
    			} else {
    				// number of elements is above the threshold - split into subtasks
    				int mid = start + (end-start) / 2;
    				FindMaxTask left = new FindMaxTask(arr, start, mid);
    				FindMaxTask right = new FindMaxTask(arr, mid, end);
    				
    				// invoke the tasks in parallel and wait for them to complete
    				invokeAll(left, right);
    				
    				// maximum of overall range is maximum of sub-ranges
    				return Math.max(left.join(), right.join());
    			}
    		}
    
    		private Integer computeDirectly() {
    			int max = Integer.MIN_VALUE;
    			for (int i = start; i < end; i++) {
    				if (arr[i] > max) {
    					max = arr[i];
    				}
    			}
    			return max;
    		}
    	}
    	
    	private static final int ARR_SIZE = 20000000;
    	
    	public static void main(String[] args) throws InterruptedException, ExecutionException {
    		// create large array of random integer values
    		int[] data = Util.makeRandomArray(ARR_SIZE);
    		
    		ForkJoinPool pool = new ForkJoinPool();
    		FindMaxTask rootTask = new FindMaxTask(data, 0, data.length);
    		Integer result = pool.invoke(rootTask);
    		
    		System.out.println("Max is " + result);
    		
    		// sequentially verify that result is correct
    		if (result.intValue() != Util.findMax(data)) {
    			System.out.println("ERROR: parallel search did not find correct maximum");
    		}
    	}
    }


