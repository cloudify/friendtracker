class AddFriendships < ActiveRecord::Migration
  def self.up
    create_table :friends, {:id => false} do |t|
      t.column :user_id, :integer, :null => false
      t.column :user_id_target, :integer, :null => false
    end
    
    create_table :invites do |t|
      t.column :user_id, :integer, :null => false
      t.column :user_id_target, :integer, :null => false
      t.column :code, :string
      t.column :message, :text
      t.column :is_accepted, :boolean
      t.column :accepted_at, :timestamp
    end
    
  end

  def self.down
    drop_table :friends
    drop_table :invites
  end
end
