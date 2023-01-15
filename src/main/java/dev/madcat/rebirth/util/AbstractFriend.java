
package dev.madcat.rebirth.util;

public abstract class AbstractFriend implements INameable, IFriendable
{
    private String name;
    private String alias;
    
    public AbstractFriend(final String name, final String alias) {
        this.name = name;
        this.alias = alias;
    }
    
    @Override
    public String getAlias() {
        return this.alias;
    }
    
    @Override
    public void setAlias(final String alias) {
        this.alias = alias;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getDisplayName() {
        return this.name;
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public void setDisplayName(final String displayName) {
        this.name = this.name;
    }
}
