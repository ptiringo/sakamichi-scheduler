package tokyo.sakamichi_noticer;

import java.util.logging.Logger;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;

import tokyo.sakamichi_noticer.eventpojos.GcsEvent;

public class UpdateSchedule implements BackgroundFunction<GcsEvent> {

  private static final Logger logger = Logger.getLogger(UpdateSchedule.class.getName());

  @Override
  public void accept(GcsEvent event, Context context) {
    logger.info("Event: " + context.eventId());
    logger.info("Event Type: " + context.eventType());
    logger.info("Bucket: " + event.getBucket());
    logger.info("File: " + event.getName());
    logger.info("Metageneration: " + event.getMetageneration());
    logger.info("Created: " + event.getTimeCreated());
    logger.info("Updated: " + event.getUpdated());
  }
}