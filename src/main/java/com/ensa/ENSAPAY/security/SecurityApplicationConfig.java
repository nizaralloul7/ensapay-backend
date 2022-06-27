package com.ensa.ENSAPAY.security;

import com.ensa.ENSAPAY.entities.Admin;
import com.ensa.ENSAPAY.entities.Agent;
import com.ensa.ENSAPAY.entities.Creditor;
import com.ensa.ENSAPAY.entities.CreditorType;
import com.ensa.ENSAPAY.security.auth.ApplicationUserService;

import com.ensa.ENSAPAY.security.jwt.JwtAuthenticationUsernameAndPasswordFilter;
import com.ensa.ENSAPAY.security.jwt.JwtTokenVerifier;
import com.ensa.ENSAPAY.services.AdminService;
import com.ensa.ENSAPAY.services.AgentService;
import com.ensa.ENSAPAY.services.ClientService;
import com.ensa.ENSAPAY.services.CreditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SecurityApplicationConfig extends WebSecurityConfigurerAdapter
{
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final AdminService adminService;
    private final AgentService agentService;
    private final ClientService clientService;

    private final CreditorService creditorService;

    @Autowired
    public SecurityApplicationConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, AdminService adminService, AgentService agentService, ClientService clientService, CreditorService creditorService)
    {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.adminService = adminService;
        this.agentService = agentService;
        this.clientService = clientService;
        this.creditorService = creditorService;
    }

    @PostConstruct
    public void adminCredentials()
    {


     /*   Creditor maroctelecom = new Creditor();
        maroctelecom.setTitle("MAROC TELECOM");
        maroctelecom.setDescription("Maroc Telecom, votre opérateur global de téléphonie fixe, mobile et internet.");
        maroctelecom.setType(CreditorType.Creditor);
        maroctelecom.setLogo("https://i.ibb.co/SsRPP8t/maroc-telecom-bleu-fr-grande.jpg");
        creditorService.addCreditor(maroctelecom);

        Creditor inwi = new Creditor();
        inwi.setTitle("INWI");
        inwi.setDescription("Découvrez les offres et services inwi, opérateur téléphonique mobile, fixe et internet au Maroc");
        inwi.setType(CreditorType.Creditor);
        inwi.setLogo("https://i.ibb.co/r0kLRbR/logo-seo.jpg");
        creditorService.addCreditor(inwi);

        Creditor orange = new Creditor();
        orange.setTitle("ORANGE");
        orange.setDescription("Bienvenue à tous chez Orange. Orange propose des offres et services de téléphonie mobile, d'internet et de fibre.");
        orange.setType(CreditorType.Creditor);
        orange.setLogo("https://i.ibb.co/mBkGK2n/Accueil.png");
        creditorService.addCreditor(orange);

        Creditor radeema = new Creditor();
        radeema.setTitle("RADEEMA");
        radeema.setDescription("RADEEMA instaure des mesures préventives pour assurer la continuité des services.");
        radeema.setType(CreditorType.Creditor);
        radeema.setLogo("https://i.ibb.co/r6hncRn/NL101-RADEEMA.jpg");
        creditorService.addCreditor(radeema);

        Creditor redal = new Creditor();
        redal.setTitle("REDAL");
        redal.setDescription("Le service e-facture est un service de Redal qui permet aux clients de recevoir leurs factures sur leurs adresses mail.");
        redal.setType(CreditorType.Creditor);
        redal.setLogo("https://i.ibb.co/mGv240N/Redal-2.jpg");
        creditorService.addCreditor(redal);

        Creditor alcs = new Creditor();
        alcs.setTitle("ALCS");
        alcs.setDescription("L'ALCS est la première et la plus importante association de lutte contre le sida constituée dans la région du Maghreb et du Moyen Orient.");
        alcs.setType(CreditorType.Charity);
        alcs.setLogo("https://i.ibb.co/QNDF5QY/images.jpg");
        creditorService.addCreditor(alcs);

           Admin admin   = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ADMIN");
            adminService.addAdmin(admin);

            Admin admin2   = new Admin();
            admin2.setUsername("nizar");
            admin2.setPassword(passwordEncoder.encode("motdepasse"));
            admin2.setRole("ADMIN");
            adminService.addAdmin(admin2);



      */


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        http
                //Defining all ant matchers and Api access ...
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationUsernameAndPasswordFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(),JwtAuthenticationUsernameAndPasswordFilter.class)
                .authorizeRequests()
                .antMatchers("/*")
                .permitAll()
                .antMatchers("/api/client/create-request")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);

        return provider;
    }
}
