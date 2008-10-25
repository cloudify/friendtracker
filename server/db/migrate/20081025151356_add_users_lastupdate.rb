class AddUsersLastupdate < ActiveRecord::Migration
  def self.up
    add_column :users, :location_updated_at, :timestamp
  end

  def self.down
    remove_column :users, :location_updated_at
  end
end
