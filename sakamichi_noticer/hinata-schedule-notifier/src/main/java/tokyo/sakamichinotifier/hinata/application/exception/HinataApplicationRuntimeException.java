package tokyo.sakamichinotifier.hinata.application.exception;

/** アプリケーションの汎用的な実行時例外 */
public class HinataApplicationRuntimeException extends RuntimeException {

	/**
	 * アプリケーションの汎用的な実行時例外の作成。
	 * @param cause 原因例外
	 */
	public HinataApplicationRuntimeException(Throwable cause) {
		super(cause);
	}

}
