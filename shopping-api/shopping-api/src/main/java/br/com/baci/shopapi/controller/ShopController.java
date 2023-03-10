package br.com.baci.shopapi.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.baci.shopapi.model.ShopDTO;
import br.com.baci.shopapi.repository.ReportRepository;
import br.com.baci.shopapi.service.ShopService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Controller
@RequestMapping("/shopping")
public class ShopController {
    
    @Autowired
    private ShopService shopService;        

    @GetMapping
    public ResponseEntity<List<ShopDTO>>  getShops(){
        List<ShopDTO> shops = shopService.getAll();
        return ResponseEntity.ok().body(shops);
    }

    @GetMapping("/shopByUser/{userIdentifier}")
    public ResponseEntity<List<ShopDTO>> getShops(@PathVariable String userIdentifier){
        List<ShopDTO> shops = shopService.getByUser(userIdentifier);
        return ResponseEntity.ok().body(shops);        
    }

    @GetMapping("/shopByDate")
    public ResponseEntity<List<ShopDTO>> getShops(@Valid @RequestBody ShopDTO shopDTO){ // Criar um DTO especifico
        List<ShopDTO> shops = shopService.getByDate(shopDTO);
        return ResponseEntity.ok().body(shops);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        ShopDTO shopDTO = shopService.findById(id);
        return ResponseEntity.ok().body(shopDTO);
    }

    @PostMapping
    public ResponseEntity<ShopDTO> newShop(@Valid @RequestBody ShopDTO shopDTO, UriComponentsBuilder uriComponentsBuilder){
        // Shop shop = Shop.converter(shopDTO);
        shopService.save(shopDTO);
        URI uri = uriComponentsBuilder.path("shopByUser/{userIdentifier}")
                    .buildAndExpand(shopDTO.userIdentifier()).toUri();        
        return ResponseEntity.created(uri).body(shopDTO);
    // public ResponseEntity<ShopDadosCadastroDTO> newShop(@Valid @RequestBody ShopDadosCadastroDTO shopDadosCadastroDTO, UriComponentsBuilder uriComponentsBuilder){
        //shopService.save(shopDadosCadastroDTO);
        // URI uri = uriComponentsBuilder.path("shopByUser/{userIdentifier}")
        //             .buildAndExpand(shopDadosCadastroDTO.userIdentifier()).toUri();        
        // return ResponseEntity.created(uri).body(shopDadosCadastroDTO);    
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShopDTO>> getShopsByFilter(
        @RequestParam(name="dataInicio", required=true)
        @DateTimeFormat(pattern="dd/MM/yy")
        LocalDate dataInicio,
        @RequestParam(name="dataFim", required=false)
        @DateTimeFormat(pattern="dd/MM/yy")
        LocalDate dataFim,
        @RequestParam(name="total", required=false)
        Float total
    ){
        List<ShopDTO> shopsDTO = shopService.getShopsByFilter(dataInicio, dataFim, total);
        return ResponseEntity.ok().body(shopsDTO);
    }
}