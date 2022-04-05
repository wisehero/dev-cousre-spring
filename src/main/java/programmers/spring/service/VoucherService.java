package programmers.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import programmers.spring.repository.VoucherRepository;
import programmers.spring.voucher.Voucher;

import java.text.MessageFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;


    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository
                .findById(voucherId)
                .orElseThrow(() ->
                        new RuntimeException(MessageFormat.format("Cannot find a voucher for {0}", voucherId)));
    }

    public void useVoucher(Voucher voucher) {

    }
}
