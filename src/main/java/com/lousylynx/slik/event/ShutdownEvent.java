package com.lousylynx.slik.event;

import com.lousylynx.slik.environment.Environment;
import lombok.Data;

@Data
public class ShutdownEvent {

    private final Environment env;
}
