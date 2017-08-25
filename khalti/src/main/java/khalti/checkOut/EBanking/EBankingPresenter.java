package khalti.checkOut.EBanking;

import android.support.annotation.NonNull;

import com.utila.EmptyUtil;
import com.utila.GuavaUtil;
import com.utila.NumberUtil;
import com.utila.StringUtil;
import com.utila.ValidationUtil;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.chooseBank.BankPojo;
import khalti.checkOut.api.ErrorAction;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.utils.DataHolder;

class EBankingPresenter implements EBankingContract.Listener {
    @NonNull
    private final EBankingContract.View mEBankingView;
    private EBankingModel eBankingModel;
    private List<BankPojo> bankLists;
    private OnCheckOutListener onCheckOutListener;

    EBankingPresenter(@NonNull EBankingContract.View mEBankingView) {
        this.mEBankingView = GuavaUtil.checkNotNull(mEBankingView);
        mEBankingView.setListener(this);
        eBankingModel = new EBankingModel();
        onCheckOutListener = DataHolder.getConfig().getOnCheckOutListener();
    }

    @Override
    public void setUpLayout(boolean hasNetwork) {
        mEBankingView.toggleButton(false);
        mEBankingView.showBankField();
        mEBankingView.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(DataHolder.getConfig().getAmount())));
        if (hasNetwork) {
            mEBankingView.toggleProgressBar(true);
            eBankingModel.fetchBankList(new EBankingModel.BankAction() {
                @Override

                public void onCompleted(Object bankList) {
                    mEBankingView.toggleButton(true);
                    mEBankingView.toggleProgressBar(false);
                    if (bankList instanceof HashMap) {
                        HashMap<?, ?> map = (HashMap<?, ?>) bankList;
                        mEBankingView.setUpSpinner(map.get("name"), map.get("idx"));
                    } else {
                        List<BankPojo> banks = (List<BankPojo>) bankList;
                        bankLists = banks;
                        mEBankingView.setUpBankItem(banks.get(0).getName(), banks.get(0).getIdx());
                    }
                }

                @Override
                public void onError(String message) {
                    mEBankingView.toggleProgressBar(false);
                    mEBankingView.showError(message);
                    onCheckOutListener.onError(ErrorAction.FETCH_BANK_LIST.getAction(), message);
                }
            });
        } else {
            mEBankingView.showNetworkError();
        }
    }

    @Override
    public void toggleEditTextListener(boolean set) {
        mEBankingView.toggleEditTextListener(set);
    }

    @Override
    public void setErrorAnimation() {
        mEBankingView.setErrorAnimation();
    }

    @Override
    public void openBankList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("banks", bankLists);
        mEBankingView.openBankList(map);
    }

    @Override
    public void updateBankItem(String bankName, String bankId) {
        mEBankingView.setUpBankItem(bankName, bankId);
    }

    @Override
    public void initiatePayment(boolean isNetwork, String mobile, String bankId, String bankName) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            if (isNetwork) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("bankId", bankId);
                map.put("bankName", bankName);

                mEBankingView.openEBanking(map);
            } else {
                mEBankingView.showNetworkError();
            }
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                mEBankingView.setMobileError("This field is required");
            } else {
                mEBankingView.setMobileError("Invalid mobile number");
            }
        }
    }
}