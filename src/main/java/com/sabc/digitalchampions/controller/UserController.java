package com.sabc.digitalchampions.controller;

import com.sabc.digitalchampions.services.UserService;
import com.sabc.digitalchampions.entity.User;
import com.sabc.digitalchampions.security.jwt.JwtUtils;
import com.sabc.digitalchampions.security.payload.response.ResponseModel;
import com.sabc.digitalchampions.utils.codegenerator.RbCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired private UserService userService;

    @GetMapping("/secure/hello")
    public ResponseEntity<ResponseModel<?>> helloSecured(){
        return ResponseEntity.ok(new ResponseModel<>("Hello world: Page secured", HttpStatus.OK));
    }

    @GetMapping("/api/hello")
    public ResponseEntity<ResponseModel<?>> helloInsecure(){
        return ResponseEntity.ok(new ResponseModel<>("Hello word: unsecured page",HttpStatus.OK));
    }


    @PostMapping("/api/login")
    public ResponseEntity<?> authenticateUser( @RequestBody User utilisateur){
        return ResponseEntity.ok(this.userService.login(utilisateur));
    }

    @PostMapping("/api/register")
    public ResponseEntity<ResponseModel<?>> registerUser( @RequestBody User utilisateur) {
        RbCodeGenerator codeGenerator = new RbCodeGenerator();
        utilisateur.setRef(codeGenerator.generate());

        if (userService.existsByUsername(utilisateur.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseModel<>("This email is already in use. Please choose another", HttpStatus.CONFLICT));
        }

        try {
            User userTmp = userService.register(utilisateur);
            return ResponseEntity.ok(new ResponseModel<>("Successfully Registered",HttpStatus.OK,userTmp));
        }catch(Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseModel<>("An error occurred while trying to register this user (please contact our support if this error persists)", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/api/user")
    public ResponseEntity<ResponseModel<Page<User>>> findAllUsers(@RequestParam(name = "firstName")Optional<String> firstName){

        return ResponseEntity.ok(
                new ResponseModel<Page<User>>(
                        userService.getUsers(firstName, PageRequest.of(0,10))
                )
        );
    }

    @GetMapping("/api/user/{ref}")
    public ResponseEntity<ResponseModel<?>> findUserByRef(@PathVariable(name="ref") String ref){
        if (!userService.existsByRef(ref)){
            return ResponseEntity.badRequest().body(
                    new ResponseModel<>(
                            "Reference Not found",
                            HttpStatus.NOT_FOUND
                    )
            );
        }else {
            return ResponseEntity.ok(
                    new ResponseModel<User>(
                            userService.findByRef(ref)
                    )
            );
        }
    }

    @PutMapping("/api/user/{ref}")
    public ResponseEntity<ResponseModel<?>> updateUser(@RequestBody User user, @PathVariable(name="ref") String ref){
        if (!userService.existsByRef(ref)){
            return ResponseEntity.badRequest().body(
                    new ResponseModel<>(
                            "Reference Not found",
                            HttpStatus.NOT_FOUND
                    )
            );
        }else{
            try {
                User tmpUser = userService.findByRef(user.getRef());
                user.setRole(tmpUser.getRole())
                        .setRef(tmpUser.getRef())
                        .setLastUpdatedAt(new Date())
                        .setPassword(tmpUser.getPassword())
                        .setId(tmpUser.getId());
                tmpUser = userService.saveUser(user);
                return ResponseEntity.ok(
                        new ResponseModel<>(tmpUser)
                );
            }catch (Exception e){
                return ResponseEntity.status(500).body(
                        new ResponseModel<>("And Error occurred while trying to register this user. Contact the support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
                );
            }

        }
    }

    @PostMapping("/api/user")
    public ResponseEntity<ResponseModel<?>> createUser(@RequestBody User user){
        if (userService.existsByUsername(user.getUsername())){
            return ResponseEntity.badRequest().body(
                    new ResponseModel<>("This email is already in use", HttpStatus.CONFLICT)
            );
        }
        User userTmp;
        try{
            userTmp = userService.saveUser(user);
            return ResponseEntity.ok(new ResponseModel<>(userTmp));
        }catch(Exception e){
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("And Error occured while trying to registre this user. Contact the support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @DeleteMapping("/api/user/{ref}")
    public ResponseEntity<ResponseModel<?>> deleteUser( @PathVariable(name = "ref") String ref){
        if (!userService.existsByRef(ref)){
            return ResponseEntity.badRequest().body(
                    new ResponseModel<>(
                            "Reference Not found",
                            HttpStatus.NOT_FOUND
                    )
            );
        }

        try{
            User user = userService.findByRef(ref);
            userService.delete(user);
            if (!userService.existsByRef(ref)){
                return ResponseEntity.ok(
                        new ResponseModel<>("Successfully deleted", HttpStatus.OK)
                );
            }else{
                return ResponseEntity.status(500).body(
                        new ResponseModel<>("And Error occurred while trying to delete this user. Contact the support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
                );
            }
        }catch(Exception e){
            return ResponseEntity.status(500).body(
                    new ResponseModel<>("And Error occurred while trying to delete this user. Contact the support if the problem persist", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }
}

