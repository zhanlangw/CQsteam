package cn.bytecloud.steam.actor.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.bytecloud.steam.actor.actor.Appactor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Data
@Service
public class DefaultActorService {

    private ActorSystem system;

    private ActorRef appactor;


    @PostConstruct
    public void initActor() {
        system = ActorSystem.create("steam");
        appactor = system.actorOf(Props.create(Appactor.class), "appActor");
    }
}
