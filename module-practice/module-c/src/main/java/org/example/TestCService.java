package org.example;


import io.misterspex.executor.scientist.Experiment;
import io.misterspex.executor.scientist.Result;
import org.springframework.stereotype.Service;

@Service
public class TestCService {
    public Response testA() {
        return new Response("success", "testA");
    }

    public Response testB() {
        return new Response("success", "testB");
    }

    public void experimentTest() throws Exception {
        Experiment<Response> experiment = new Experiment<>() {
            @Override
            protected void publish(Result result) {
                super.publish(result);
                if (result.candidateObservation().isPresent()) {
                    System.out.println("candidate : " + (Response) result.candidateObservation().get());
                }
                System.out.println("control : " + result.controlObservation());
            }
        };
        Response execute = experiment.execute(
                this::testA,
                this::testB
        );

    }
}
