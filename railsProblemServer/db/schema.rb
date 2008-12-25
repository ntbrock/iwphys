# This file is autogenerated. Instead of editing this file, please use the
# migrations feature of ActiveRecord to incrementally modify your database, and
# then regenerate this schema definition.

ActiveRecord::Schema.define(:version => 1) do

  create_table "sessions", :force => true do |t|
    t.column "session_id", :string
    t.column "data",       :text
    t.column "updated_at", :datetime
  end

  add_index "sessions", ["session_id"], :name => "index_sessions_on_session_id"
  add_index "sessions", ["updated_at"], :name => "index_sessions_on_updated_at"

  create_table "user_configurations", :force => true do |t|
    t.column "email_confirmation", :integer,   :limit => 3, :default => 1,  :null => false
    t.column "email_sender",       :text,                   :default => "", :null => false
    t.column "created_on",         :timestamp
    t.column "updated_on",         :timestamp
  end

  create_table "users", :force => true do |t|
    t.column "login",        :string,    :limit => 40,  :default => "", :null => false
    t.column "name",         :string,    :limit => 128, :default => "", :null => false
    t.column "admin",        :integer,   :limit => 1,   :default => 0,  :null => false
    t.column "activated",    :integer,   :limit => 1,   :default => 0,  :null => false
    t.column "email",        :string,    :limit => 80,  :default => "", :null => false
    t.column "cypher",       :text,                     :default => "", :null => false
    t.column "salt",         :string,    :limit => 40,  :default => "", :null => false
    t.column "token",        :string,    :limit => 10,  :default => "", :null => false
    t.column "token_expiry", :timestamp
    t.column "created_on",   :timestamp
    t.column "updated_on",   :timestamp
    t.column "lock_version", :integer,                  :default => 0,  :null => false
  end

  add_index "users", ["login"], :name => "login"
  add_index "users", ["email"], :name => "email"

end
