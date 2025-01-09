package kr.hhplus.be.server.application.in;

public record UserWalletCommand(
        long userId
) {
    public static UserWalletCommand from(long userId) {
        return new UserWalletCommand(
                userId
        );
    }

}
