package kr.hhplus.be.server.interfaces.response;


public record UserWalletResponse(
        long userId,
        long currentAmount
) {
//    public static UserWalletResponse from(UserWallet userWallet){
//        return new UserWalletResponse(
//                userWallet.getUserId(),
//                userWallet.getCurrentAmount()
//        );
//    }

    public static UserWalletResponse mock(long userId){
        return new UserWalletResponse(
                userId,
                100_000L
        );
    }
}
