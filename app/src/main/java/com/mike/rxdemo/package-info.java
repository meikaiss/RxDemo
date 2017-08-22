
/**
 * 简单装笔的说
 * 可观察数据流进行异步编程的编程接口，帮助开发者更方便地编写异步和基于事件的程序，
 * 让你从混乱的回调中解脱出来，写出更高可读性、更不容易产生Bug的代码
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * *****什么是ReactiveX*****
 * <p>
 * ReactiveX(Reactive Extensions)，一般简写为Rx。是一个使用可观察数据流进行异步编程的编程接口
 * Rx是一个编程模型，目标是提供一致的编程接口，帮助开发者更方便地编写异步和基于事件的程序
 * ReactiveX不仅仅是一个API，它是一种思想、一种编程突破，它影响了许多其它的API、框架甚至编程语言。
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * *****为什么要使用ReactiveX*****
 * <p>
 * 无论你开发app还是后台应用，你总会时不时地编写一些异步或基于事件的代码，
 * 但你会发现很难完美地处理工作线程和主线程的切换、异常的处理、线程的取消、线程同步等等问题，
 * 而且在多个线程协调处理业务逻辑时代码结构变得异常的复杂而且还容易出错。
 * 使用Rx，你可以：
 * <p>
 * 函数式编程
 * 对可观察数据流使用无副作用的输入输出函数，避免了程序里错综复杂的状态
 * <p>
 * 精简代码
 * Rx的操作符经常可以将复杂的难题简化为很少的几行代码
 * <p>
 * 更好地处理异步错误
 * 传统的try/catch对于异步计算过程中的错误无能为力，但Rx提供了很好的错误处理机制
 * <p>
 * 轻松处理并发
 * Rx的Observable和Scheduler让开发者可以摆脱底层的线程同步和各种并发问题
 * <p>
 * <p>
 * 也就是说，ReactiveX的Observable模型让你对异步事件流的处理就像平时对数据集(如数组)进行简单、可组合的处理一样，
 * 让你从混乱的回调中解脱出来，写出更高可读性、更不容易产生Bug的代码。
 * Rx结合了观察者模式、迭代器模式和函数式编程的优秀思想。Rx扩展观察者模式以支持数据/事件序列，
 * 添加了一些操作符以使你可以声明式的组合这些序列，而无需关注底层的实现：如线程、同步、线程安全、并发数据结构和非阻塞IO 。
 */
