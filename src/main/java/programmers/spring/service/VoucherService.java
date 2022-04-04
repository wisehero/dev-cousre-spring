package programmers.spring.service;

import programmers.spring.repository.VoucherRepository;
import programmers.spring.voucher.Voucher;

import java.text.MessageFormat;
import java.util.UUID;

public class VoucherService {

    private final VoucherRepository voucherRepository;

    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository
                .findById(voucherId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Cannot find a voucher for {0}", voucherId));
    }

    public void useVoucher(Voucher voucher) {

    }
}
