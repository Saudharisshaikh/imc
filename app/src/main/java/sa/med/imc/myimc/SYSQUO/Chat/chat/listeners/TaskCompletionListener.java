package sa.med.imc.myimc.SYSQUO.Chat.chat.listeners;

public interface TaskCompletionListener<T, U> {

  void onSuccess(T t);

  void onError(U u);
}
