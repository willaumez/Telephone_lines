package com.telephone.backendtelephonelines.services;

import com.telephone.backendtelephonelines.dtos.*;
import com.telephone.backendtelephonelines.entities.*;
import com.telephone.backendtelephonelines.exceptions.LigneTelephoniqueNotFoundException;
import com.telephone.backendtelephonelines.exceptions.UserNotFoundException;
import com.telephone.backendtelephonelines.mappers.LigneTelephoniqueMappers;
import com.telephone.backendtelephonelines.repositories.LigneTelephoniqueRepository;
import com.telephone.backendtelephonelines.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class LigneTelephoniqueServiceImpl implements LigneTelephoniqueService {

    private LigneTelephoniqueRepository ligneTelephoniqueRepository;
    private UserRepository userRepository;
    private LigneTelephoniqueMappers dtoMapper;


    /*public LigneTelephoniqueServiceImpl(LigneTelephoniqueMappers dtoMapper, LigneTelephoniqueRepository ligneTelephoniqueRepository) {
        this.dtoMapper = dtoMapper;
        this.ligneTelephoniqueRepository = ligneTelephoniqueRepository;
    }*/


    @Override
    public User saveUser(User user) {
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("----- User Not found -----"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        if (user.getPassword() != null) {
            String password = user.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchUser(String keyword) {
        return userRepository.searchUser(keyword);
    }

    //==========================================================================================================//


    @Override
    public InternetMobileVPNDTO saveInternetMobileVPN(InternetMobileVPNDTO internetMobileVPNDTO) {
        InternetMobileVPN internetMobileVPN = dtoMapper.fromInternetMobileVPNDTO(internetMobileVPNDTO);
        InternetMobileVPN savedMobileVPN = ligneTelephoniqueRepository.save(internetMobileVPN);
        return dtoMapper.fromInternetMobileVPN(savedMobileVPN);
    }

    @Override
    public InternetMobileDTO saveInternetMobile(InternetMobileDTO internetMobileDTO) {
        System.out.println("saveInternetMobile--"+internetMobileDTO);
        InternetMobile internetMobile = dtoMapper.fromInternetMobileDTO(internetMobileDTO);
        InternetMobile savedMobile = ligneTelephoniqueRepository.save(internetMobile);
        return dtoMapper.fromInternetMobile(savedMobile);
    }

    @Override
    public GsmDTO saveGsm(GsmDTO gsmDTO) {
        Gsm gsm = dtoMapper.fromGsmDTO(gsmDTO);
        Gsm savedGsm = ligneTelephoniqueRepository.save(gsm);
        return dtoMapper.fromGsm(savedGsm);
    }

    @Override
    public FixVpnAdslVpnLLDTO saveFixVpnAdslVpnLL(FixVpnAdslVpnLLDTO fixVpnAdslVpnLLDTO) {
        FixVpnAdslVpnLL fixVpnAdslVpnLL = dtoMapper.fromFixVpnAdslVpnLLDTO(fixVpnAdslVpnLLDTO);
        FixVpnAdslVpnLL savedFix = ligneTelephoniqueRepository.save(fixVpnAdslVpnLL);
        return dtoMapper.fromFixVpnAdslVpnLL(savedFix);
    }

    @Override
    public LigneTelephoniqueDTO getLigneTelephonique(Long id) throws LigneTelephoniqueNotFoundException {
        LigneTelephonique ligneTelephonique = ligneTelephoniqueRepository.findById(id)
                .orElseThrow(() -> new LigneTelephoniqueNotFoundException("Ligne Telephonique Introuvable!"));
        if (ligneTelephonique instanceof InternetMobile){
            InternetMobile internetMobile = (InternetMobile) ligneTelephonique;
            return dtoMapper.fromInternetMobile(internetMobile);
        } else if (ligneTelephonique instanceof InternetMobileVPN){
            InternetMobileVPN internetMobileVPN = (InternetMobileVPN) ligneTelephonique;
            return dtoMapper.fromInternetMobileVPN(internetMobileVPN);
        } else if (ligneTelephonique instanceof Gsm){
            Gsm gsm = (Gsm) ligneTelephonique;
            return dtoMapper.fromGsm(gsm);
        } else {
            FixVpnAdslVpnLL fixVpnAdslVpnLL = (FixVpnAdslVpnLL) ligneTelephonique;
            return dtoMapper.fromFixVpnAdslVpnLL(fixVpnAdslVpnLL);
        }
    }


    @Override
    public List<LigneTelephoniqueDTO> ligneTelephoniqueList() {
        List<LigneTelephonique> ligneTelephoniques = ligneTelephoniqueRepository.findAll();
        return ligneTelephoniques.stream().map(ligneTelephonique -> {
            if (ligneTelephonique instanceof InternetMobile internetMobile){
                return dtoMapper.fromInternetMobile(internetMobile);
            } else if (ligneTelephonique instanceof InternetMobileVPN internetMobileVPN){
                return dtoMapper.fromInternetMobileVPN(internetMobileVPN);
            } else if (ligneTelephonique instanceof Gsm gsm){
                return dtoMapper.fromGsm(gsm);
            } else {
                FixVpnAdslVpnLL fixVpnAdslVpnLL = (FixVpnAdslVpnLL) ligneTelephonique;
                return dtoMapper.fromFixVpnAdslVpnLL(fixVpnAdslVpnLL);
            }
        }).collect(Collectors.toList());
    }

    /*@Override
    public List<LigneTelephoniqueDTO> ligneTelephoniqueList() {
        List<LigneTelephonique> ligneTelephoniques = ligneTelephoniqueRepository.findAll();
        List<LigneTelephoniqueDTO> ligneTelephoniqueDTOS = ligneTelephoniques.stream().map(ligneTelephonique -> {
            if (ligneTelephonique instanceof InternetMobile){
                InternetMobile internetMobile = (InternetMobile) ligneTelephonique;
                return dtoMapper.fromInternetMobile(internetMobile);
            } else if (ligneTelephonique instanceof InternetMobileVPN){
                InternetMobileVPN internetMobileVPN = (InternetMobileVPN) ligneTelephonique;
                return dtoMapper.fromInternetMobileVPN(internetMobileVPN);
            } else if (ligneTelephonique instanceof Gsm){
                Gsm gsm = (Gsm) ligneTelephonique;
                return dtoMapper.fromGsm(gsm);
            } else {
                FixVpnAdslVpnLL fixVpnAdslVpnLL = (FixVpnAdslVpnLL) ligneTelephonique;
                return dtoMapper.fromFixVpnAdslVpnLL(fixVpnAdslVpnLL);
            }
        }).collect(Collectors.toList());
        return ligneTelephoniqueDTOS;
    }*/

    @Override
    public List<LigneTelephoniqueDTO> searchLigneTelephonique(String keyword) {
        List<LigneTelephonique> ligneTelephoniques = ligneTelephoniqueRepository.searchLigneTelephonique(keyword);
        return ligneTelephoniques.stream().map(ligneTelephonique -> {
            if (ligneTelephonique instanceof InternetMobile internetMobile){
                return dtoMapper.fromInternetMobile(internetMobile);
            } else if (ligneTelephonique instanceof InternetMobileVPN internetMobileVPN){
                return dtoMapper.fromInternetMobileVPN(internetMobileVPN);
            } else if (ligneTelephonique instanceof Gsm gsm){
                return dtoMapper.fromGsm(gsm);
            } else {
                FixVpnAdslVpnLL fixVpnAdslVpnLL = (FixVpnAdslVpnLL) ligneTelephonique;
                return dtoMapper.fromFixVpnAdslVpnLL(fixVpnAdslVpnLL);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteLigneTelephonique(Long id) {
        ligneTelephoniqueRepository.deleteById(id);
    }


    //update
    @Override
    public InternetMobileVPNDTO updateInternetMobileVPN(InternetMobileVPNDTO internetMobileVPNDTO) {
        InternetMobileVPN internetMobileVPN = dtoMapper.fromInternetMobileVPNDTO(internetMobileVPNDTO);
        InternetMobileVPN saveInternetVpn = ligneTelephoniqueRepository.save(internetMobileVPN);
        return dtoMapper.fromInternetMobileVPN(saveInternetVpn);
    }

    @Override
    public InternetMobileDTO updateInternetMobile(InternetMobileDTO internetMobileDTO) {
        InternetMobile internetMobile = dtoMapper.fromInternetMobileDTO(internetMobileDTO);
        InternetMobile saveInternet = ligneTelephoniqueRepository.save(internetMobile);
        return dtoMapper.fromInternetMobile(saveInternet);
    }

    @Override
    public GsmDTO updateGsm(GsmDTO gsmDTO) {
        Gsm gsm = dtoMapper.fromGsmDTO(gsmDTO);
        Gsm saveGsm = ligneTelephoniqueRepository.save(gsm);
        return dtoMapper.fromGsm(saveGsm);
    }

    @Override
    public FixVpnAdslVpnLLDTO updateFix(FixVpnAdslVpnLLDTO fixVpnAdslVpnLLDTO) {
        FixVpnAdslVpnLL fixVpnAdslVpnLL = dtoMapper.fromFixVpnAdslVpnLLDTO(fixVpnAdslVpnLLDTO);
        FixVpnAdslVpnLL saveFix = ligneTelephoniqueRepository.save(fixVpnAdslVpnLL);
        return dtoMapper.fromFixVpnAdslVpnLL(saveFix);
    }

    //get Type
    @Override
    public List<LigneTelephoniqueDTO> getTypeLigneTelephonique(String typeLigne){
        List<LigneTelephonique> ligneTelephoniques = ligneTelephoniqueRepository.findByType(typeLigne);
        return ligneTelephoniques.stream().map(ligneTelephonique -> {
            if (ligneTelephonique instanceof InternetMobile internetMobile){
                return dtoMapper.fromInternetMobile(internetMobile);
            } else if (ligneTelephonique instanceof InternetMobileVPN internetMobileVPN){
                return dtoMapper.fromInternetMobileVPN(internetMobileVPN);
            } else if (ligneTelephonique instanceof Gsm gsm){
                return dtoMapper.fromGsm(gsm);
            } else {
                FixVpnAdslVpnLL fixVpnAdslVpnLL = (FixVpnAdslVpnLL) ligneTelephonique;
                return dtoMapper.fromFixVpnAdslVpnLL(fixVpnAdslVpnLL);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username, username);
    }

    /*@Override
    public boolean confirmPassword(Long id, String password) throws UserNotFoundException {
        User user = userRepository.findById(id) .orElseThrow(() -> new UserNotFoundException("----- User Not found -----"));;
        String passwordUser = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        if (passwordUser === passwordEncoder){
            return true;
        }else return false;
    }*/

    @Override
    public boolean confirmPassword(Long id, String password) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));

        String passwordUser = user.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("matchesmatchesmatchesmatches---"+passwordEncoder.matches(password, passwordUser));

        return passwordEncoder.matches(password, passwordUser);
    }


    //Rapprochement
    @Override
    public List<Rapprochement> rapprochementList() {
        List<LigneTelephonique> ligneTelephoniques = ligneTelephoniqueRepository.findAll();
        return ligneTelephoniques.stream().map(ligneTelephonique -> {
            Rapprochement rapprochement = new Rapprochement();
            rapprochement.setNumero(ligneTelephonique.getNumeroLigne());
            rapprochement.setMontant(ligneTelephonique.getMontant());
            return rapprochement;
        }).collect(Collectors.toList());
    }


}

