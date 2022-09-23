package white.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import white.entity.AddressBook;
import white.mapper.AddressBookMapper;
import white.service.AddressBookService;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>implements AddressBookService {
}
