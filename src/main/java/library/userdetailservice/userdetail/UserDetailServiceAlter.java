package library.userdetailservice.userdetail;

import io.jsonwebtoken.Jwts;
import library.exception.exception.ExistException;
import library.exception.exception.NullException;
import library.jpa.entity.Card;
import library.jpa.entity.User;
import library.jpa.enum_.StatusCard;
import library.jpa.repository.UserRepository;
import library.jpa.service.CardService;
import library.security.DAO.FormAdminEdit;
import library.security.DAO.FormEditUser;
import library.security.DAO.FormRegister;
import library.security.DAO.InformationUserDao;
import library.security.configuration.jwt_config.JwtPropertiesConfiguration;
import library.userdetailservice.model.Account;
import library.userdetailservice.model.Authority;
import library.userdetailservice.model.Role;
import library.userdetailservice.repository.RoleRepository;
import library.userdetailservice.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserDetailServiceAlter implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final CardService cardService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecretKey secretKey;
    private final JwtPropertiesConfiguration jwtPropertiesConfiguration;
    private final PasswordEncoder passwordEncoder;

    public UserDetailServiceAlter(AccountRepository userRepository, CardService cardRepository, UserRepository userRepository1, RoleRepository roleRepository, SecretKey secretKey, JwtPropertiesConfiguration jwtPropertiesConfiguration, PasswordEncoder passwordEncoder) {
        this.accountRepository = userRepository;
        this.cardService = cardRepository;
        this.userRepository = userRepository1;
        this.roleRepository = roleRepository;
        this.secretKey = secretKey;
        this.jwtPropertiesConfiguration = jwtPropertiesConfiguration;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    public Account getByID(Long id) {
        return accountRepository.getOne(id);
    }

    // *** good ***
    public InformationUserDao getProfile(HttpServletRequest httpServletRequest) {
        // get username
        String username = getUsernameFromRequest(httpServletRequest);

        // get account with username
        Account account = accountRepository.findByUsername(username);
        return new InformationUserDao(account);
    }

    // *** good ***
    public InformationUserDao editProfile(HttpServletRequest httpServletRequest, FormEditUser dao) {
        // get username from http servlet request
        String username = getUsernameFromRequest(httpServletRequest);

        // find account use repository account
        Account account = accountRepository.findByUsername(username);

        // user is entity save information of account so edit user
        User user = userRepository.getOne(account.getIdUser());
        user.setInformation(dao);
        userRepository.save(user);

        // create Information response

        return new InformationUserDao(account);
    }

    // *** good ***
    public InformationUserDao createNewAccount(FormRegister userDao) {
        // if username is exist throw exception
        if (accountRepository.existsByUsername(userDao.getUsername()))
            throw new ExistException("This Username '" + userDao.getUsername() + "' is existed");

        // create information with empty for account
        User user = new User(userDao);
        userRepository.save(user);
        Role role = roleRepository.findByName("USER");
        Card card = cardService.createNewCard();

        // encrypt password to new account
        Account account = new Account(userDao.getUsername(),
                passwordEncoder.encode(userDao.getPassword()), role.getId());
        account.setIdUser(user.getId());
        account.setIdCard(card.getId());

        // save account
        accountRepository.save(account);
        account.setUserInformation(user);
        account.setRoleInformation(role);
        account.setCardInformation(card);

        return new InformationUserDao(account);
    }

    // *** ok ***
    public InformationUserDao adminSetRole(FormAdminEdit formAdminEdit) {
        preExceptionIdOrUsername(formAdminEdit.getUsername(), formAdminEdit.getIdAccount());

        if (formAdminEdit.getIdRole() == null)
            throw new NullException("ID Role is null. Enter 'idRole'");

        // get account by username or id from request
        Account account = getAccountByIdOrUsername(formAdminEdit.getUsername(), formAdminEdit.getIdAccount());

        // set ID Role account to change role
        account.setIdRole(formAdminEdit.getIdRole());

        // set role to account to response to client
        Role role = roleRepository.findById(account.getIdRole()).get();
        account.setRoleInformation(role);

        // save change of account to database
        accountRepository.save(account);

        return new InformationUserDao(account);
    }

    // *** ok ***
    public InformationUserDao createCard(FormAdminEdit formAdminEdit) {
        preExceptionIdOrUsername(formAdminEdit.getUsername(), formAdminEdit.getIdAccount());

        // get account switch username or id from request
        Account account = getAccountByIdOrUsername(formAdminEdit.getUsername(), formAdminEdit.getIdAccount());

        // set status card of user is miss
        Card cardMiss = cardService.getByID(account.getIdCard());
        cardMiss.setStatus(StatusCard.MISS.getStatus());
        cardService.update(cardMiss);

        // create new card
        Card card = cardService.createNewCard();
        card.setId_account(account.getId());

        // set ID Role account to change
        account.setIdCard(card.getId());

        // set role to account to response to client
        account.setCardInformation(card);

        // save account to database
        accountRepository.save(account);

        return new InformationUserDao(account);
    }


    // *** good ***
    @Override
    public UserDetails loadUserByUsername(String username) {
        // find account use repository
        Account user = accountRepository.findByUsername(username);

        //check not found
        if (user == null) {
            throw new UsernameNotFoundException("loadUserByUsername from UserDetailService throw UsernameNOtFoundException. check your username !");
        }
        //print information userDetails
        debug(user);

        return new UserDetailAlter(user);
    }

    // *** good ***
    public void debug(Account user) {
        System.out.println("\nname: " + user.getUsername());
        System.out.println("pass: " + user.getPassword());
        System.out.println("role: " + user.getRoleInformation().getName() + "\nauthority:");
        for (Authority auth : user.getRoleInformation().getAuthorities()) {
            System.out.println("    " + auth.getAuthority());
        }
    }

    // *** good ***
    public String getUsernameFromRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace(jwtPropertiesConfiguration.getTokenPrefix(), "");

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public void preExceptionIdOrUsername(String username, Long id) {
        if (username == null && id == null)
            throw new NullException("account ID and Username is null. Enter 'idAccount' or 'username' to request.");
        if (!accountRepository.existsByUsername(username) && username != null)
            throw new ExistException("This Username '" + username + "' is not existed");
        if (id != null && !accountRepository.existsById(id))
            throw new ExistException("This User ID '" + id + "' is not existed");
    }

    public Account getAccountByIdOrUsername(String username, Long id) {
        if (username != null) {
            return accountRepository.findByUsername(username);
        } else {
            return accountRepository.findById(id).get();
        }
    }
}
