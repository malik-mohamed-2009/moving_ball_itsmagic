void repeat() {
    myObject.getPosition().setZ(myObject.getPosition().getZ() - Math.bySecond(5));
    myObject.lookTo(WorldController.findObject("Player"));
    
    if (myObject.getPosition().getZ() < -10) {
        myObject.destroy();
    }
}